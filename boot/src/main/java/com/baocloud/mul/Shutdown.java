package com.baocloud.mul;

import java.util.concurrent.TimeUnit;

public class Shutdown {
	private static class Runner implements Runnable{
		private long i;
		private volatile boolean on=true;

		@Override
		public void run() {
			while(on && !Thread.currentThread().isInterrupted()){
				i++;
			}
			System.out.println("Count i = "+i);
		}
		
		public void cancel(){
			on=false;
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		Runner runner=new Runner();
		Thread countThread=new Thread(runner);
		countThread.start();
		TimeUnit.SECONDS.sleep(10);
		countThread.interrupt();
		
		Runner two=new Runner();
		countThread=new Thread(two);
		countThread.start();
		TimeUnit.SECONDS.sleep(10);
		two.cancel();
		
	}

}
