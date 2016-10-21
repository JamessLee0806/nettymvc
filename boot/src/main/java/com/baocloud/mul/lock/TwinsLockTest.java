package com.baocloud.mul.lock;

import java.util.concurrent.locks.Lock;

import com.baocloud.mul.SleepUtils;

public class TwinsLockTest {
	private static class Worker extends Thread {
		private Lock lock;

		public Worker(Lock lock) {
			this.lock = lock;
		}

		@Override
		public void run() {
			while (true) {
				lock.lock();
				try {
					SleepUtils.second(1);
					System.out.println(Thread.currentThread().getName());
					SleepUtils.second(1);
				} finally {
					lock.unlock();
				}
			}
		}
	}

	public static void main(String[] args) {
		Lock lock = new TwinsLock();
		for (int i = 0; i < 10; i++) {
			Worker w = new Worker(lock);
			w.setDaemon(true);
			w.start();
		}
		// 每隔1秒换行
		for (int i = 0; i < 10; i++) {
			SleepUtils.second(1);
			System.out.println();
		}
	}

}
