package com.baocloud.netty.chap10.mvc;

import javax.servlet.Servlet;
import javax.servlet.ServletContext;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.stream.ChunkedStream;
import io.netty.util.CharsetUtil;
import static io.netty.handler.codec.http.HttpResponseStatus.INTERNAL_SERVER_ERROR;
import static io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map.Entry;

import static io.netty.handler.codec.http.HttpHeaderNames.CONTENT_TYPE;
public class ServletNettyHandler extends SimpleChannelInboundHandler<FullHttpRequest> {

	private final Servlet servlet;
	private final ServletContext servletContext;

	public ServletNettyHandler(Servlet servlet) {
		this.servlet = servlet;
		this.servletContext = servlet.getServletConfig().getServletContext();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, FullHttpRequest msg) throws Exception {
		if(!msg.decoderResult().isSuccess()){
			sendError(ctx, BAD_REQUEST);
		}
		MockHttpServletRequest httpRequst=createServletRequest(msg);
		MockHttpServletResponse httpResponse=new MockHttpServletResponse();
		this.servlet.service(httpRequst, httpResponse);
		HttpResponseStatus status=HttpResponseStatus.valueOf(httpResponse.getStatus());
		FullHttpResponse nettyResponse=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status);
		for(String name:httpResponse.getHeaderNames()){
			for(String value:httpResponse.getHeaders(name)){
				nettyResponse.headers().add(name, value);
			}
		}
		ByteBuf info=Unpooled.copiedBuffer(httpResponse.getContentAsByteArray());
		nettyResponse.content().writeBytes(info);
		ChannelFuture future=ctx.writeAndFlush(nettyResponse);
		future.addListener(ChannelFutureListener.CLOSE);

	}
	
	
	private MockHttpServletRequest createServletRequest(FullHttpRequest request){
		UriComponents uriComponents=UriComponentsBuilder.fromPath(request.uri()).build();//UriComponentsBuilder.fromHttpUrl(request.uri()).build();
		MockHttpServletRequest mockReqeust=new MockHttpServletRequest(servletContext);
		mockReqeust.setRequestURI(uriComponents.getPath());
		mockReqeust.setPathInfo(uriComponents.getPath());
		mockReqeust.setMethod(request.method().name().toString());
		if(uriComponents.getScheme()!=null)
			mockReqeust.setScheme(uriComponents.getScheme());
		if(uriComponents.getHost()!=null)
			mockReqeust.setServerName(uriComponents.getHost());
		if(uriComponents.getPort()!=-1)
			mockReqeust.setServerPort(uriComponents.getPort());
		for(CharSequence name:request.headers().names()){
			for(CharSequence value:request.headers().getAll(name)){
				mockReqeust.addHeader(name.toString(), value.toString());
			}
		}
		mockReqeust.setContent(request.content().array());
		
		try {
			if(uriComponents.getQuery()!=null){
				String query=UriUtils.decode(uriComponents.getQuery(),"UTF-8");
				mockReqeust.setQueryString(query);
			}
			for(Entry<String, List<String>> entry:uriComponents.getQueryParams().entrySet()){
				for(String value:entry.getValue()){
					mockReqeust.addParameter(UriUtils.decode(entry.getKey(), "UTF-8"),UriUtils.decode(value, "UTF-8"));
				}
			}
				
		} catch (Exception e) {
			// TODO: handle exception
		}
		return mockReqeust;
		
		
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		if(ctx.channel().isActive()){
			sendError(ctx, INTERNAL_SERVER_ERROR);
		}
	}

	private static void sendError(ChannelHandlerContext ctx,HttpResponseStatus status){
		FullHttpResponse response=new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, status,Unpooled.copiedBuffer("Falture:"+status.toString()+"\r\n",CharsetUtil.UTF_8));
		response.headers().set(CONTENT_TYPE, "text/plain; charset=UTF-8");
		ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE);
		
		
		
	}

}
