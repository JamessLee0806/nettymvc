package com.baocloud.netty.chap11;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

public class WebSocketServer {

	static void bind() throws Exception {
		EventLoopGroup parentGroup = new NioEventLoopGroup();
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(parentGroup, workGroup);
			server.channel(NioServerSocketChannel.class);
			server.option(ChannelOption.SO_BACKLOG, 1024);
			server.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ChannelPipeline pipeLine = ch.pipeline();
					pipeLine.addLast("http-codec", new HttpServerCodec());
					pipeLine.addLast("aggrator", new HttpObjectAggregator(65535));
					pipeLine.addLast("http-chunked", new ChunkedWriteHandler());
					pipeLine.addLast("websocket", new WebSocketServerHandler());

				}
			});
			ChannelFuture future = server.bind(8080).sync();
			System.out.println("Web socket server started at port 8080.");
			System.out.println("Open your browser and navigate to http://localhost:8080/");
			future.channel().closeFuture().sync();

		} finally {
			parentGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	public static void main(String[] args)  throws Exception{
		bind();
	}

}
