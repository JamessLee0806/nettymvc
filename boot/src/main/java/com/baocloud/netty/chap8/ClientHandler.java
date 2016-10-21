package com.baocloud.netty.chap8;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ClientHandler extends ChannelHandlerAdapter {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		//cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		for(int i=0;i<19;i++){
			ctx.write(create(i));
		}
		ctx.flush();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		System.out.println("receive response ["+msg+"]");
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}
	
	
	private  SubscribeReqProto.SubscribeReq create(int seq){
		SubscribeReqProto.SubscribeReq.Builder builder=SubscribeReqProto.SubscribeReq.newBuilder();
		builder.setSubReqId(seq);
		builder.setUserName("James Lee");
		builder.setProductName("Netty In Action");
		builder.addAddress("nanjing jiangning");
		builder.addAddress("anhui Bozhou");
		builder.addAddress("jiangsu xuzhou");
		return builder.build();
	}

}
