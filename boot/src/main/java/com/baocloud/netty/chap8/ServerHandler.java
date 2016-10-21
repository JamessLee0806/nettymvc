package com.baocloud.netty.chap8;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		System.out.println("errrrrrrrrrrrr");
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		SubscribeReqProto.SubscribeReq req = (SubscribeReqProto.SubscribeReq) msg;
		System.out.println("recevie a req -> " + req.toString());
		ctx.writeAndFlush(resp(req));
	}

	private SubscribeRespProto.SubscribeResp resp(SubscribeReqProto.SubscribeReq req) {
		SubscribeRespProto.SubscribeResp.Builder builder = SubscribeRespProto.SubscribeResp.newBuilder();
		builder.setSubReqID(req.getSubReqId());
		builder.setRespCode(1);
		builder.setDesc("Order created succefully.");
		SubscribeRespProto.SubscribeResp resp= builder.build();
		System.out.println(resp);
		return resp;
	}

}
