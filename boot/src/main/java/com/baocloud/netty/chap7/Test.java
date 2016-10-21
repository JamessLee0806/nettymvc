package com.baocloud.netty.chap7;

public class Test {
	public static void main(String[] args) {
       new Thread(new Server(8080), "Server").start();
       new Thread(new Client("127.0.0.1", 8080), "Client").start();
	}
}
