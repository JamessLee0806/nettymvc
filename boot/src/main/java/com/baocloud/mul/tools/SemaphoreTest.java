package com.baocloud.mul.tools;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class SemaphoreTest {
	private static class Task implements Runnable {
		private Semaphore semaphore;

		public Task(Semaphore semaphore) {
			this.semaphore = semaphore;
		}

		@Override
		public void run() {
			try {
				semaphore.acquire();
				System.out.println(Thread.currentThread().getName() + " save data......");
				semaphore.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

		}

	}
	
	public static void main(String[] args) {
		int cpus=Runtime.getRuntime().availableProcessors();
		ExecutorService threadPool=Executors.newFixedThreadPool(cpus*3);
		Semaphore semaphore=new Semaphore(cpus);
		for(int i=0;i<3*cpus;i++){
			threadPool.execute(new Task(semaphore));
		}
		threadPool.shutdown();
	}
}
