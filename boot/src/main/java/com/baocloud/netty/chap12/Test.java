package com.baocloud.netty.chap12;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Test {
	public static void main(String[] args) throws InterruptedException {
		Thread server=new Thread(new ServerTask(), "server");
		server.start();
		ExecutorService pool=Executors.newFixedThreadPool(20);
		for(int i=0;i<20;i++){
			pool.execute(new ClientTask());
		}
		pool.shutdown();
		System.out.println("dddddddd");
	}

}
