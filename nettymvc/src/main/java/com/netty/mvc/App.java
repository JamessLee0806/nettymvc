package com.netty.mvc;

import com.netty.mvc.server.NettyServer;

public class App {
	public static void main(String[] args) throws Exception {
		//NettyServer.bind();
		double dd=60;
		int  c=(int)Math.ceil(((dd / 100) * 399900));
		System.out.println(c);
		double rant=(60.0 / 100);
		System.out.println(rant);
		System.out.println(Math.ceil(((dd / 100) * 399900)));
	}

}
