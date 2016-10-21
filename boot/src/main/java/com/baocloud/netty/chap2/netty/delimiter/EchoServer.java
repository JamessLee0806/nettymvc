package com.baocloud.netty.chap2.netty.delimiter;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class EchoServer {
	private static class Server implements Runnable {
		private final int port;

		public Server(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			EventLoopGroup bossGroup = new NioEventLoopGroup(1);
			EventLoopGroup workGroup = new NioEventLoopGroup(Runtime.getRuntime().availableProcessors());
			try {
				ServerBootstrap boot = new ServerBootstrap();
				boot.group(bossGroup, workGroup);
				boot.channel(NioServerSocketChannel.class);
				boot.option(ChannelOption.SO_BACKLOG, 1000);
				boot.handler(new LoggingHandler(LogLevel.DEBUG));
				boot.childHandler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new ServerHandler());
					}
				});
				ChannelFuture f = boot.bind(this.port).sync();
				f.channel().closeFuture().sync();

			} catch (Exception e) {
				bossGroup.shutdownGracefully();
				workGroup.shutdownGracefully();
			} finally {
			}

		}

	}

	private static class ServerHandler extends ChannelHandlerAdapter {
		private int counter = 0;

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			String body = (String) msg;
			System.out.println("This is " + ++counter + " time receive client: [ " + body + " ]");
			body += "$_";
			ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
			ctx.writeAndFlush(echo);
		}

	}

	private static class Client implements Runnable {
		private final int port;
		private final String host;

		public Client(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public void run() {
			EventLoopGroup workGroup = new NioEventLoopGroup();
			try {
				Bootstrap client = new Bootstrap();
				client.group(workGroup);
				client.channel(NioSocketChannel.class);
				client.option(ChannelOption.TCP_NODELAY, true);
				client.handler(new ChannelInitializer<SocketChannel>() {

					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ByteBuf delimiter = Unpooled.copiedBuffer("$_".getBytes());
						ch.pipeline().addLast(new DelimiterBasedFrameDecoder(1024, delimiter));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new ClientHandler());
					}

				});
				ChannelFuture f = client.connect(host, port).sync();
				f.channel().closeFuture().sync();

			} catch (Exception e) {
				e.printStackTrace();
			}

			finally {
				workGroup.shutdownGracefully();
			}

		}

	}

	private static class ClientHandler extends ChannelHandlerAdapter {
		private final String msg = "Hello,James Lee to netty world.$_";
		private int counter = 0;

		public ClientHandler() {

		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			for (int i = 0; i < 100; i++) {
				ctx.writeAndFlush(Unpooled.copiedBuffer(msg.getBytes()));
			}
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
			System.out.println("This is " + ++counter + " times receive servier: [ " + msg + "]");
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}

	}

	public static void main(String[] args) {
		new Thread(new Server(8080), "Server").start();
		new Thread(new Client("127.0.0.1", 8080), "Client").start();
	}

}
