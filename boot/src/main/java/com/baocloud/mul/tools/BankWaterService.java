package com.baocloud.mul.tools;

import java.util.Map.Entry;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class BankWaterService implements Runnable {
	private CyclicBarrier c = new CyclicBarrier(4, this);
	ExecutorService threadPool = Executors.newFixedThreadPool(4);
	private ConcurrentHashMap<String, Integer> sheetBankWaterCount = new ConcurrentHashMap<>();

	private void count() {
		for (int i = 0; i < 4; i++) {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {
					sheetBankWaterCount.put(Thread.currentThread().getName(), 1);
					try {
						c.await();
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (BrokenBarrierException e) {
						e.printStackTrace();
					}

				}
			});
		}
	}

	@Override
	public void run() {
		int result = 0;
		for (Entry<String, Integer> entry : sheetBankWaterCount.entrySet()) {
			result += entry.getValue();
		}
		sheetBankWaterCount.put("result", result);
		System.out.println("result=" + result);
	}

	public static void main(String[] args) {
		BankWaterService service = new BankWaterService();
		service.count();
		service.threadPool.shutdown();
	}

}
