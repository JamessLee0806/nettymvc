package com.baocloud.netty.chap7;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server implements Runnable {
	private int port;
	
	public Server(int port){
		this.port=port;
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup=new NioEventLoopGroup(1);
		EventLoopGroup workGroup=new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
		try {
			ServerBootstrap server=new ServerBootstrap();
			server.group(bossGroup, workGroup);
			server.channel(NioServerSocketChannel.class);
			server.option(ChannelOption.SO_BACKLOG, 1024);
			server.handler(new LoggingHandler(LogLevel.DEBUG));
			server.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ObjectDecoder(1024*1024, ClassResolvers.softCachingConcurrentResolver(this.getClass().getClassLoader())));
					ch.pipeline().addLast(new ObjectEncoder());
					ch.pipeline().addLast(new ServerHandler());
				}
			});
			ChannelFuture futer=server.bind(port).sync();
			futer.channel().closeFuture().sync();
		}catch(Exception e){
			
		}finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}

}
