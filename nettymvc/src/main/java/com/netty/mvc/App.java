package com.netty.mvc;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import com.netty.mvc.server.WebConfig;
import com.netty.mvc.sps.beans.SingleObj;

public class App {
	public static void main(String[] args) throws Exception {
		AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(WebConfig.class);
		SingleObj objA = ctx.getBean("singleBean", SingleObj.class);
		SingleObj objB = ctx.getBean("singleBean", SingleObj.class);
		System.out.println(objA == objB);
		System.out.println(objA.equals(objB));

		ctx.close();
	}

}
