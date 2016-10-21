package com.baocloud.netty.chap2.netty;

import java.sql.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;

public class TimeServer {
	private static class Server implements Runnable {
		private int port;

		public Server(int p) {
			this.port = p;
		}

		@Override
		public void run() {
			EventLoopGroup reactorGroup = new NioEventLoopGroup(1);
			EventLoopGroup workerGroup = new NioEventLoopGroup();
			try {
				ServerBootstrap b = new ServerBootstrap();
				b.group(reactorGroup, workerGroup);
				b.channel(NioServerSocketChannel.class);
				b.option(ChannelOption.SO_BACKLOG, 1024);
				b.childHandler(new ServerChannelHandler());
				ChannelFuture future = b.bind(this.port).sync();
				future.channel().closeFuture().sync();
			} catch (Exception e) {
			} finally {
				reactorGroup.shutdownGracefully();
				workerGroup.shutdownGracefully();
			}

		}

	}

	private static class ServerChannelHandler extends ChannelInitializer<SocketChannel> {

		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
			ch.pipeline().addLast(new StringDecoder());
			ch.pipeline().addLast(new ServerHandler());
		}

	}

	private static class ServerHandler extends ChannelHandlerAdapter {
		private int counter;
		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			cause.printStackTrace();
			ctx.close();
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//			ByteBuf byteBuf = (ByteBuf) msg;
//			byte[] req = new byte[byteBuf.readableBytes()];
//			byteBuf.readBytes(req);
			String body =(String) msg;//= new String(req, "UTF-8").substring(0,req.length-System.getProperty("line.separator").length());
			System.out.println("The time server receive order: "+body +" ;the counter is "+ ++counter);
			System.out.println("server receive req:" + body);
			String currentTime = "QUERY".equals(body) ? new Date(System.currentTimeMillis()).toString() : "BAD FORMAT";
			currentTime=currentTime+System.getProperty("line.separator");
			ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
			ctx.write(resp);
		}

		@Override
		public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
			ctx.flush();
		}

	}

	private static class Client implements Runnable {
		private int port;

		public Client(int port) {
			this.port = port;
		}

		@Override
		public void run() {
			EventLoopGroup workGroup = new NioEventLoopGroup();
			try {
				Bootstrap b = new Bootstrap();
				b.group(workGroup);
				b.channel(NioSocketChannel.class);
				b.option(ChannelOption.TCP_NODELAY, true);
				b.handler(new ChannelInitializer<SocketChannel>() {
					@Override
					protected void initChannel(SocketChannel ch) throws Exception {
						ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
						ch.pipeline().addLast(new StringDecoder());
						ch.pipeline().addLast(new ClientTimeHandler());
					}

				});
				ChannelFuture f = b.connect("127.0.0.1", port).sync();
				f.channel().closeFuture().sync();

			} catch (Exception e) {
				e.printStackTrace();

			} finally {
				workGroup.shutdownGracefully();
			}
		}

	}

	private static class ClientTimeHandler extends ChannelHandlerAdapter {
		private final Logger logger = LoggerFactory.getLogger(this.getClass().getName());
		//private final ByteBuf fisrtMsg;
		private int counter;
		private byte[] req;

		public ClientTimeHandler() {
			req = ("QUERY"+System.getProperty("line.separator")).getBytes();
			//fisrtMsg = Unpooled.buffer(req.length);
			//fisrtMsg.writeBytes(req);
			logger.info("create Handler........");
		}

		@Override
		public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
			logger.warn("Execetion:" + cause.getMessage());
			ctx.close();
		}

		@Override
		public void channelActive(ChannelHandlerContext ctx) throws Exception {
			logger.info("Active..........");
			ByteBuf msg=null;
			for(int i=0;i<100;i++){
				msg=Unpooled.buffer(req.length);
				msg.writeBytes(req);
				ctx.writeAndFlush(msg);
			}
			
		}

		@Override
		public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
//			ByteBuf byteBuf = (ByteBuf) msg;
//			byte[] req = new byte[byteBuf.readableBytes()];
//			byteBuf.readBytes(req);
			String body = (String)msg;//new String(req, "UTF-8");
			logger.info("Now is " + body+"; the counter is "+ ++counter);
		}

	}

	public static void main(String[] args) {
		new Thread(new Server(8080)).start();
		new Thread(new Client(8080)).start();
	}

}
