package com.baocloud.netty.chap10.file;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class HttpFileServer {
	protected final static String DEFAULT_PATH = "/app/wkpros/boot/src/";

	public static void bind(int port, String url) throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup(1);
		EventLoopGroup workGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap server=new ServerBootstrap();
			server.group(bossGroup, workGroup);
			server.channel(NioServerSocketChannel.class);
			server.option(ChannelOption.SO_BACKLOG, 1024);
			server.childHandler(new ChannelInitializer<SocketChannel>() {

				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast("http-decoder", new HttpRequestDecoder());
					ch.pipeline().addLast("http-aggregator",new HttpObjectAggregator(65536));
					ch.pipeline().addLast("http-encoder",
						    new HttpResponseEncoder());
					    ch.pipeline().addLast("http-chunked",
						    new ChunkedWriteHandler());
					ch.pipeline().addLast("fileHandler",new HttpFileServerHandler(url));
				}
			});
			ChannelFuture f=server.bind("192.168.199.165",port).sync();
			System.out.println("HttpServer start....网址为：http://192.168.199.165:8080"+url);
			f.channel().closeFuture().sync();

		} finally {
			bossGroup.shutdownGracefully();
			workGroup.shutdownGracefully();
		}

	}
	
	
	public static void main(String[] args) throws Exception {
		bind(8080, DEFAULT_PATH);
	}
}
