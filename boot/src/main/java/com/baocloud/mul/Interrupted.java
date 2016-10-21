package com.baocloud.mul;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

public class Interrupted {
	
	static class SleepRunner implements Runnable{

		@Override
		public void run() {
			while(true){
				try {
					TimeUnit.SECONDS.sleep(1);
				} catch (InterruptedException e) {
				}
			}
			
		}
		
	}
	
	static class BusyRunner implements Runnable{
		@Override
		public void run() {
			BigInteger i=new BigInteger("0");
			while (true) {
				i=i.add(new BigInteger("1"));
				if(Thread.currentThread().isInterrupted()){
					System.out.println("I have received a interupped");
					@SuppressWarnings("static-access")
					boolean interrupted = Thread.currentThread().interrupted();
					System.out.println("in"+interrupted);
				}
			}
			
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		Thread sleepThread=new Thread(new SleepRunner(),"SleepThread");
		sleepThread.setDaemon(true);
		sleepThread.start();
		
		Thread busyThread=new Thread(new BusyRunner(), "BusyThread");
		busyThread.setDaemon(true);
		busyThread.start();
		
		TimeUnit.SECONDS.sleep(1);
		
		sleepThread.interrupt();
		busyThread.interrupt();
		System.out.println("SleepThread is interupted:"+sleepThread.isInterrupted());
		System.out.println("BusyThread is interupted:"+busyThread.isInterrupted());
		
		TimeUnit.SECONDS.sleep(1);
		
	}

}
