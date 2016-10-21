package com.baocloud.netty.chap12;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.socket.DatagramPacket;
import io.netty.util.CharsetUtil;

public class ClientHandler extends SimpleChannelInboundHandler<DatagramPacket> {

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		cause.printStackTrace();
		ctx.close();
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, DatagramPacket msg) throws Exception {
		String resp=msg.content().toString(CharsetUtil.UTF_8);
		if(resp.startsWith("谚语查询结果:")){
			System.out.println(resp);
			ctx.close();
		}
	}

}
