package com.baocloud.netty.chap10.mvc;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.mock.web.MockServletConfig;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpRequestDecoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import io.netty.handler.stream.ChunkedWriteHandler;

public class DispatcherServletChannelInitializer extends ChannelInitializer<SocketChannel> {

	private final DispatcherServlet servlet;

	public DispatcherServletChannelInitializer() throws Exception {
		MockServletContext ctx = new MockServletContext();
		ctx.setContextPath("/");
		ctx.setDefaultServletName("springMvc");
		MockServletConfig config = new MockServletConfig(ctx);
		AnnotationConfigWebApplicationContext wac=new AnnotationConfigWebApplicationContext();
		wac.setServletConfig(config);
		wac.setServletContext(ctx);
		wac.register(WebConfig.class);
		wac.refresh();
		servlet = new DispatcherServlet(wac);
		servlet.init(config);

	}

	@Override
	protected void initChannel(SocketChannel ch) throws Exception {
		ChannelPipeline cp=ch.pipeline();
		cp.addLast("decoder",new HttpRequestDecoder());
		cp.addLast("aggrator",new HttpObjectAggregator(65536));
		cp.addLast("encoder", new HttpResponseEncoder());
		cp.addLast("chunk", new ChunkedWriteHandler());
		cp.addLast("httpHandler", new ServletNettyHandler(servlet));
	}
	
	@Configuration
	@EnableWebMvc
	@ComponentScan(basePackages="com.baocloud.netty.chap10.mvc")
	static class WebConfig extends WebMvcConfigurerAdapter{
		
	}

}
