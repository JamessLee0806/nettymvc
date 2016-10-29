package com.netty.mvc.server;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
@Configuration
@EnableWebMvc
@EnableAspectJAutoProxy
@ComponentScan(basePackages="com.netty.mvc.sps")
public class WebConfig extends WebMvcConfigurerAdapter{

}
