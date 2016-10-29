package com.netty.mvc;

import com.netty.mvc.server.NettyServer;

public class App {
	public static void main(String[] args) throws Exception {
		NettyServer.bind();
	}

}
