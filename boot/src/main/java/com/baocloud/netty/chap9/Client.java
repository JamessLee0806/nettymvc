package com.baocloud.netty.chap9;

import com.baocloud.netty.chap7.ClientHandler;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class Client implements Runnable {

	private int port;
	private String host;
	
	public Client(String h,int p){
		this.host=h;
		this.port=p;
	}

	@Override
	public void run() {
		EventLoopGroup workGroup=new NioEventLoopGroup();
		try {
			Bootstrap client=new Bootstrap();
			client.group(workGroup);
			client.channel(NioSocketChannel.class);
			client.option(ChannelOption.TCP_NODELAY,true);
			client.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(MarshallingCodecFactory.buildMarshallingDecoder());
					ch.pipeline().addLast(MarshallingCodecFactory.buildMarshalingEncoder());
					ch.pipeline().addLast(new ClientHandler());
				}
			});
			ChannelFuture f= client.connect(host, port).sync();
			f.channel().closeFuture().sync();
		} catch(Exception e){
			
		}finally {
			workGroup.shutdownGracefully();
		}

	}
}
