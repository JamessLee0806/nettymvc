package com.baocloud.netty.chap12;

import java.net.InetSocketAddress;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.DatagramPacket;
import io.netty.channel.socket.nio.NioDatagramChannel;
import io.netty.util.CharsetUtil;

public class ClientTask implements Runnable {

	@Override
	public void run() {
		EventLoopGroup g = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(g);
			b.channel(NioDatagramChannel.class);
			b.option(ChannelOption.SO_BROADCAST, true);
			b.handler(new ClientHandler());
			Channel channel = b.bind(0).sync().channel();
			channel.writeAndFlush(new DatagramPacket(Unpooled.copiedBuffer("谚语字典查询?", CharsetUtil.UTF_8),
					new InetSocketAddress("255.255.255.255", 8080))).sync();
			if (!channel.closeFuture().await(15000)) {
				System.out.println("查询超时");
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			g.shutdownGracefully();
		}
	}

}
