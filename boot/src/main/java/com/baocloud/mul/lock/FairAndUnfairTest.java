package com.baocloud.mul.lock;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class FairAndUnfairTest {
	@SuppressWarnings("serial")
	private static class ReenTrantLock2 extends ReentrantLock {
		public ReenTrantLock2(boolean faireFlag) {
			super(faireFlag);
		}

		public Collection<Thread> getQueuedThreads() {
			List<Thread> arrays = new ArrayList<Thread>(super.getQueuedThreads());
			Collections.reverse(arrays);
			return arrays;
		}

		private static class Job extends Thread {
			private Lock lock;

			public Job(Lock lock) {
				this.lock = lock;
			}

			@Override
			public void run() {
				try {
					start.await();
				} catch (InterruptedException e) {
				}
				for (int i = 0; i < 2; i++) {
					lock.lock();
					try {
						System.out.println("Lock by [" + getName() + "], Waiting by "
								+ ((ReenTrantLock2) lock).getQueuedThreads());
					} finally {
						lock.unlock();
					}
				}
			}

			public String toString() {
				return getName();
			}
		}

		private static Lock fairLock = new ReenTrantLock2(true);
		private static Lock unFairLock = new ReenTrantLock2(false);
		private static CountDownLatch start;

		public static void main(String[] args) {
			//testLock(fairLock);
//			System.out.println("============================");
			testLock(unFairLock);
		}

		static void testLock(Lock lock) {
			start = new CountDownLatch(1);
			for (int i = 0; i <1000; i++) {
				Thread t = new Job(lock);
				t.setName("" + i);
				t.start();
			}
			start.countDown();
		}

	}

}
