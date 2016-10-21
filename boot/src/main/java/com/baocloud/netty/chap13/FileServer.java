package com.baocloud.netty.chap13;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.CharsetUtil;

public class FileServer {
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
					pipeLine.addLast("StringEncoder", new StringEncoder(CharsetUtil.UTF_8));
					pipeLine.addLast("line", new LineBasedFrameDecoder(1024));
					pipeLine.addLast("decoder", new StringDecoder(CharsetUtil.UTF_8));
					pipeLine.addLast("File", new FileServerHandler());
				}
			});
			ChannelFuture f = server.bind(8080).sync();

			f.channel().closeFuture().sync();

		} finally {
			parentGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}
	}
	
	
	public static void main(String[] args) throws Exception {
		bind();
	}
}
