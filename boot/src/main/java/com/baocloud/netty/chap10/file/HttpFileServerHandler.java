package com.baocloud.netty.chap10.file;

import java.io.File;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.regex.Pattern;

import javax.activation.MimetypesFileTypeMap;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelProgressiveFuture;
import io.netty.channel.ChannelProgressiveFutureListener;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.DefaultHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.LastHttpContent;
import io.netty.handler.stream.ChunkedFile;
import io.netty.util.CharsetUtil;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_LENGTH;
import static io.netty.handler.codec.http.HttpHeaderNames.CONNECTION;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;
import static io.netty.handler.codec.http.HttpResponseStatus.FOUND;
import static io.netty.handler.codec.http.HttpHeaderNames.LOCATION;
import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;
import static io.netty.handler.codec.http.HttpResponseStatus.METHOD_NOT_ALLOWED;
import static io.netty.handler.codec.http.HttpResponseStatus.FORBIDDEN;
import static io.netty.handler.codec.http.HttpResponseStatus.NOT_FOUND;
import static io.netty.handler.codec.http.HttpMethod.GET;
import static io.netty.handler.codec.http.HttpHeaderUtil.isKeepAlive;
import static io.netty.handler.codec.http.HttpHeaderValues.KEEP_ALIVE;

public class HttpFileServerHandler extends SimpleChannelInboundHandler<FullHttpRequest> {
	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.decoderResult().isSuccess()) {
			sendError(ctx, BAD_REQUEST);
			return;
		}
		if (request.method() != GET) {
			sendError(ctx, METHOD_NOT_ALLOWED);
			return;
		}

		final String uri = request.uri();
		final String path = sanitizeUri(uri);
		if (path == null) {
			sendError(ctx, FORBIDDEN);
			return;
		}
		File file = new File(path);
		System.out.println("fp="+path);
//		System.out.println(file.isHidden() +"   "+file.canRead());
//		if (file.isHidden() || !file.canRead()) {
//			sendError(ctx, NOT_FOUND);
//			return;
//		}
		if (file.isDirectory()) {
			if (uri.endsWith("/")) {
				sendList(ctx, file);
			} else {
				sendRedirect(ctx, uri + "/");
			}
			return;
		}
		if (!file.isFile()) {
			sendError(ctx, FORBIDDEN);
			return;
		}

		RandomAccessFile raf = null;
		try {
			raf = new RandomAccessFile(file, "r");
		} catch (Exception e) {
			sendError(ctx, NOT_FOUND);
			return;
		}
		long fileLength = raf.length();
		HttpResponse response = new DefaultHttpResponse(HTTP_1_1, OK);
		response.headers().set(CONTENT_LENGTH, fileLength + "");
		setContentTypeHeader(response, file);
		if (isKeepAlive(request)) {
			response.headers().set(CONNECTION, KEEP_ALIVE);
		}
		ctx.write(response);
		ChannelFuture channelFuture = null;
		channelFuture = ctx.write(new ChunkedFile(raf, 0, fileLength, 8192), ctx.newProgressivePromise());
		channelFuture.addListener(new ChannelProgressiveFutureListener() {

			@Override
			public void operationComplete(ChannelProgressiveFuture future) throws Exception {
				System.out.println("Transfer complete.");
			}

			@Override
			public void operationProgressed(ChannelProgressiveFuture future, long progress, long total)
					throws Exception {
				if (total < 0) { // total unknown
				    System.err.println("Transfer progress: " + progress);
				} else {
				    System.err.println("Transfer progress: " + progress + " / "
					    + total);
				}
			}
		});
		ChannelFuture lastFuture=ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT);
		if(!isKeepAlive(request)){
			lastFuture.addListener(ChannelFutureListener.CLOSE);
		}

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if (ctx.channel().isActive()) {
			sendError(ctx, INTERNAL_SERVER_ERROR);
		}
	}

	private String sanitizeUri(String uri) {
		try {
			uri = URLDecoder.decode(uri, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			try {
				uri = URLDecoder.decode(uri, "ISO-8859-1");
			} catch (UnsupportedEncodingException e1) {
				throw new Error();
			}
		}
		if (!uri.startsWith(url)) {
			return null;
		}
		if (!uri.startsWith("/")) {
			return null;
		}
		uri = uri.replace('/', File.separatorChar);
		if (uri.contains(File.separator + '.') || uri.contains('.' + File.separator) || uri.startsWith(".")
				|| uri.endsWith(".") || INSECURE_URI.matcher(uri).matches()) {
			return null;
		}
		return uri;
	}

	private static void sendList(ChannelHandlerContext ctx, File dir) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, OK);
		response.headers().set(CONTENT_TYPE, "text/html; charset=UTF-8");
		StringBuilder buf = new StringBuilder();
		String dirPath = dir.getPath();
		buf.append("<!DOCTYPE html>\r\n");
		buf.append("<html><head><title>");
		buf.append(dirPath);
		buf.append(" 目录：");
		buf.append("</title></head><body>\r\n");
		buf.append("<h3>");
		buf.append(dirPath).append(" 目录：");
		buf.append("</h3>\r\n");
		buf.append("<ul>");
		buf.append("<li>链接：<a href=\"../\">..</a></li>\r\n");
		for (File f : dir.listFiles()) {
			if (f.isHidden() || !f.canRead()) {
				continue;
			}
			String name = f.getName();
			if (!ALLOWED_FILE_NAME.matcher(name).matches()) {
				continue;
			}
			buf.append("<li>链接：<a href=\"");
			buf.append(name);
			buf.append("\">");
			buf.append(name);
			buf.append("</a></li>\r\n");
		}
		buf.append("</ul></body></html>\r\n");
		ByteBuf buffer = Unpooled.copiedBuffer(buf, CharsetUtil.UTF_8);
		response.content().writeBytes(buffer);
		buffer.release();
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);

	}

	private static void sendRedirect(ChannelHandlerContext ctx, String newUri) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, FOUND);
		response.headers().set(LOCATION, newUri);
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static void sendError(ChannelHandlerContext ctx, HttpResponseStatus status) {
		FullHttpResponse response = new DefaultFullHttpResponse(HTTP_1_1, status,
				Unpooled.copiedBuffer("Faiure:" + status.toString() + "\r\n", CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
	}

	private static void setContentTypeHeader(HttpResponse response, File file) {
		MimetypesFileTypeMap map = new MimetypesFileTypeMap();
		response.headers().set(CONTENT_TYPE, map.getContentType(file.getPath()));
	}

	private static final Pattern INSECURE_URI = Pattern.compile(".*[<>&\"].*");
	private static final Pattern ALLOWED_FILE_NAME = Pattern.compile("[A-Za-z0-9][-_A-Za-z0-9\\.]*");
	private final String url;

	public HttpFileServerHandler(String url) {
		this.url = url;
	}

}
