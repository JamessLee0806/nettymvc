package com.baocloud.mul.tools;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CountDownLatchTest {
	private static class Task implements Runnable {
		private CountDownLatch cdl;
		private String name;

		public Task(CountDownLatch cdl, String name) {
			this.cdl = cdl;
			this.name = name;
		}

		@Override
		public void run() {
			System.out.println(this.name + " has finished.");
			cdl.countDown();
		}

	}

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch cdl=new CountDownLatch(3);
		ExecutorService threadPool=Executors.newFixedThreadPool(3);
		for(int i=0;i<3;i++){
			Task t=new Task(cdl, "task_"+i);
			threadPool.execute(t);
		}
		cdl.await();
		System.out.println("All Task has finished.   "+cdl.getCount());
		threadPool.shutdown();
	}
}
