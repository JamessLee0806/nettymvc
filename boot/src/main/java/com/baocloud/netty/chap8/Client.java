package com.baocloud.netty.chap8;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;

public class Client implements Runnable {
	private String host;
	private int port;

	public Client(String h, int p) {
		this.host = h;
		this.port = p;
	}

	@Override
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
					ch.pipeline().addLast(new ProtobufVarint32FrameDecoder());
					ch.pipeline().addLast(new ProtobufDecoder(SubscribeRespProto.SubscribeResp.getDefaultInstance()));
					ch.pipeline().addLast(new ProtobufVarint32LengthFieldPrepender());
					ch.pipeline().addLast(new ProtobufEncoder());
					ch.pipeline().addLast(new ClientHandler());

				}
			});

			ChannelFuture f = client.connect(host, port).sync();
			f.channel().closeFuture().sync();

		} catch (Exception e) {

		} finally {
			workGroup.shutdownGracefully();
		}

	}

}
