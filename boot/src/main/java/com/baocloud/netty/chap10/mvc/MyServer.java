package com.baocloud.netty.chap10.mvc;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class MyServer {
	static void bind() throws Exception{
		EventLoopGroup bossGroup=new NioEventLoopGroup(1);
		EventLoopGroup workGroup=new NioEventLoopGroup();
		try {
			ServerBootstrap server=new ServerBootstrap();
			server.group(bossGroup, workGroup);
			server.channel(NioServerSocketChannel.class);
			server.option(ChannelOption.SO_BACKLOG, 1024);
			server.childHandler(new DispatcherServletChannelInitializer());
			ChannelFuture cf=server.bind(8080).sync();
			cf.channel().closeFuture().sync();
		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
		
		
	}
	
	public static void main(String[] args) throws Exception {
		bind();
	}

}
