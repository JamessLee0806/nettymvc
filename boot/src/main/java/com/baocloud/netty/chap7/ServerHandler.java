package com.baocloud.netty.chap7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

public class ServerHandler extends ChannelHandlerAdapter {
    Logger log=LoggerFactory.getLogger(ServerHandler.class);
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
      SubscribeReq req=(SubscribeReq)msg;
      System.out.println("server receive a sub req "+req.toString());
      ctx.writeAndFlush(resp(req.getSubReqID()));
      System.out.println("order created succeedd.......");
	}
   
	private SubscribeResp resp(int reqId){
		SubscribeResp resp=new SubscribeResp();
		resp.setSubReqId(reqId);
		resp.setRespCode("0");
		resp.setDesc("Netty book order succeed. 3 days later send to desigined address.");
		return resp;
	}
}
