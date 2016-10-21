package com.baocloud.mul.tools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExchangerTest {
	private static class Task implements Runnable{
		private Exchanger<String> exchanger;
		private String data;
		public Task(Exchanger<String> exg,String data){
			this.exchanger=exg;
			this.data=data;
		}
		@Override
		public void run() {
			System.out.println(Thread.currentThread().getName()+" send data "+data);
			String recevie=null;
			try {
				recevie = exchanger.exchange(data);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println(Thread.currentThread().getName()+" receive data "+recevie);
		}
		
	}
	
	public static void main(String[] args) {
		Exchanger<String> exg=new Exchanger<>();
		ExecutorService threadPool=Executors.newFixedThreadPool(2);
		for(int i=0;i<2;i++){
			Task t=new Task(exg, "data "+i);
			threadPool.execute(t);
		}
		threadPool.shutdown();
	}

}
