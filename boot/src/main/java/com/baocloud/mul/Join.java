package com.baocloud.mul;

import java.util.concurrent.TimeUnit;

public class Join {
	static class Domino implements Runnable {
		private Thread thread;

		public Domino(Thread t) {
			this.thread = t;
		}

		@Override
		public void run() {
			try {
				thread.join();
			} catch (Exception e) {
			}
			System.out.println(Thread.currentThread().getName() + " terminated.");

		}

	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread previous=Thread.currentThread();
		for(int i=0;i<10;i++){
			Thread thread=new Thread(new Domino(previous), String.valueOf(i));
			thread.start();
			previous=thread;
		}
		TimeUnit.SECONDS.sleep(10);
		
		System.out.println("Main "+Thread.currentThread().getName()+"terminated.");
	}
}
