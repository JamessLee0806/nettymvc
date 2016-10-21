package com.baocloud.netty.chap11;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.HttpHeaderUtil;
import io.netty.handler.codec.http.HttpResponseStatus;
import io.netty.handler.codec.http.HttpVersion;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketServerHandshakerFactory;
import io.netty.util.CharsetUtil;

public class WebSocketServerHandler extends SimpleChannelInboundHandler<Object> {

	private final Logger log = LoggerFactory.getLogger(WebSocketServerHandler.class);
	private WebSocketServerHandshaker handShaker;

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println(msg);
		System.out.println(msg.getClass().getName());
		if (msg instanceof FullHttpRequest)
			handleHttpRequest(ctx, (FullHttpRequest) msg);
		else
			handleWebSocketFrame(ctx, (WebSocketFrame) msg);
	}


	private void handleHttpRequest(ChannelHandlerContext ctx, FullHttpRequest request) throws Exception {
		if (!request.decoderResult().isSuccess() || (!"websocket".equals(request.headers().get("Upgrade")))) {
			sendHttpResponse(ctx, request,
					new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.BAD_REQUEST));
			return;
		}

		WebSocketServerHandshakerFactory fac = new WebSocketServerHandshakerFactory("ws://localhost:8080/websocket",
				null, false);
		handShaker = fac.newHandshaker(request);
		if (handShaker == null) {
			WebSocketServerHandshakerFactory.sendUnsupportedVersionResponse(ctx.channel());
		} else {
			handShaker.handshake(ctx.channel(), request);
		}

	}

	private void handleWebSocketFrame(ChannelHandlerContext ctx, WebSocketFrame frame) {
		if (frame instanceof CloseWebSocketFrame) {
			handShaker.close(ctx.channel(), (CloseWebSocketFrame) frame.retain());
			return;
		}
		if (frame instanceof PingWebSocketFrame) {
			ctx.channel().writeAndFlush(new PongWebSocketFrame(frame.content().retain()));
			return;
		}

		if (!(frame instanceof TextWebSocketFrame)) {
			throw new UnsupportedOperationException(
					String.format("%s frame types not supported", frame.getClass().getName()));
		}

		String request = ((TextWebSocketFrame) frame).text();
		if (log.isDebugEnabled()) {
			log.debug("{} received {}", ctx.channel(), request);
		}
		ctx.channel().writeAndFlush(new TextWebSocketFrame(request + ", 欢迎使用Netty WebSocket服务，现在时刻:" + new Date().toString()));

	}

	private static void sendHttpResponse(ChannelHandlerContext ctx, FullHttpRequest request, FullHttpResponse response)
			throws Exception {
		if (response.status().code() != 200) {
			ByteBuf buffer = Unpooled.copiedBuffer(response.status().toString(), CharsetUtil.UTF_8);
			response.content().writeBytes(buffer);
			buffer.release();
			HttpHeaderUtil.setContentLength(response, response.content().readableBytes());
		}
		ChannelFuture channelFuture = ctx.channel().writeAndFlush(response);
		if (!HttpHeaderUtil.isKeepAlive(request) || response.status().code() != 200) {
			channelFuture.addListener(ChannelFutureListener.CLOSE);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

}
