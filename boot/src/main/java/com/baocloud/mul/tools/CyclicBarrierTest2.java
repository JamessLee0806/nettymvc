package com.baocloud.mul.tools;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.baocloud.mul.SleepUtils;

public class CyclicBarrierTest2 {
	private static class Task implements Runnable {
		private CyclicBarrier clb;

		public Task(CyclicBarrier clb) {
			this.clb = clb;
		}

		@Override
		public void run() {
			try {
				System.out.println(Thread.currentThread().getName()+"arrived.");
				clb.await();
				SleepUtils.second(1);
				System.out.println(Thread.currentThread().getName()+"finished.");
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (BrokenBarrierException e) {
				e.printStackTrace();
			}

		}
	}
	
	private static class ArriveTask implements Runnable{

		@Override
		public void run() {
			System.out.println(this.getClass().getName()+" has execute...");
			
		}
		
	}
	
	public static void main(String[] args) {
		int cpus=Runtime.getRuntime().availableProcessors();
		CyclicBarrier clb=new CyclicBarrier(cpus,new ArriveTask());
		ExecutorService threadPool=Executors.newFixedThreadPool(cpus);
		for(int i=0;i<cpus;i++){
			Task t=new Task(clb);
			threadPool.execute(t);
		}
		threadPool.shutdown();
	}


}
