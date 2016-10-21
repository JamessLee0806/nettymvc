package com.baocloud.netty.chap12;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioDatagramChannel;

public class ServerTask implements Runnable {
	@Override
	public void run() {
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.channel(NioDatagramChannel.class);
			b.option(ChannelOption.SO_BROADCAST, true);
			b.handler(new ServerHandler());
			b.bind(8080).sync().channel().closeFuture().await();

		} catch (InterruptedException e) {
			e.printStackTrace();
		} finally {
			group.shutdownGracefully();
		}

	}

}
