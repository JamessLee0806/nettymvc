package com.baocloud.netty.chap7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter {
	private Logger logger = LoggerFactory.getLogger(ClientHandler.class);

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for (int i = 0; i < 20; i++) {
			ctx.write(req(i));
		}
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SubscribeResp resp = (SubscribeResp) msg;
		logger.info("order receive .." + resp.toString());

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	private SubscribeReq req(int id) {
		SubscribeReq req = new SubscribeReq();
		req.setSubReqID(id);
		req.setAddress("南京江宁区太平花苑南区62栋");
		req.setPdtName("Netty in Action");
		req.setPhoneNumber("18551744396");
		req.setUserName("李井华");

		return req;
	}

}
