package com.baocloud.netty.chap8;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server implements Runnable {
	private int port;

	public Server(int p) {
		this.port = p;
	}

	@Override
	public void run() {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGroup, workGroup);
			server.channel(NioServerSocketChannel.class);
			server.option(ChannelOption.SO_BACKLOG, 1024);
			server.handler(new LoggingHandler(LogLevel.TRACE));
			server.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast(new ProtobufDecoder(SubscribeReqProto.SubscribeReq.getDefaultInstance()));
					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast(new ProtobufEncoder());
					ch.pipeline().addLast(new ServerHandler());

				}
			});
			ChannelFuture f = server.bind(port).sync();
			f.channel().closeFuture().sync();
		} catch (Exception e) {

		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}

}
