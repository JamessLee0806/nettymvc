package com.baocloud.netty.chap2.netty.fix;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.FixedLengthFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {

	private static class ServerHandler extends ChannelHandlerAdapter {

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("Receive client: [" + msg + "]");
		}

	}

	public static void bind(int port) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workGroup);
			server.channel(NioServerSocketChannel.class);
			server.option(ChannelOption.SO_BACKLOG, 1024);
			server.handler(new LoggingHandler(LogLevel.DEBUG));
			server.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new FixedLengthFrameDecoder(200));
					ch.pipeline().addLast(new StringDecoder());
					ch.pipeline().addLast(new ServerHandler());
				}
			});
			ChannelFuture f = server.bind(port).sync();
			f.channel().closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		bind(8080);
	}

}
