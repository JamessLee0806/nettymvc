package com.baocloud.mul;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockExample {
	public static class LockObj {
		private Lock lock = new ReentrantLock();
		int i = 0;

		public void addValue() {
			lock.lock();
			try {
				i++;
			} finally {
				lock.unlock();
			}
		}

		public int getValue() {
			lock.lock();
			try {
				return i;
			} finally {
				lock.unlock();
			}
		}

	}

	public static class ReadTask implements Runnable {
		private LockObj obj;

		public ReadTask(LockObj obj) {
			this.obj = obj;
		}

		@Override
		public void run() {
			while (true) {
				System.out.println("Get Value " + obj.getValue());
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static class AddTask implements Runnable {
		private LockObj obj;

		public AddTask(LockObj obj) {
			this.obj = obj;
		}

		@Override
		public void run() {
			while (true) {
				obj.addValue();
				System.out.println("ADD Value ... ");
				try {
					Thread.sleep(2000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}

	public static class Monitor implements Runnable {
		private Thread[] threads;

		public Monitor(Thread[] threads) {
			this.threads = threads;
		}

		@Override
		public void run() {
			boolean allClose = true;
			while (true) {
				for (Thread t : threads) {
					if (t.isAlive())
						allClose = false;
					System.out.println("Thread "+t.getName()+" status is "+t.getState());
				}

				if (allClose)
					return;
				try {
					Thread.sleep(1000L);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

		}

	}
	
	public static void main(String[] args) {
		Thread[] threads=new Thread[4];
		LockObj obj=new LockObj();
		threads[0]=new Thread(new AddTask(obj), "Add1");
		threads[1]=new Thread(new AddTask(obj), "Add2");
		
		threads[2]=new Thread(new ReadTask(obj), "Read1");
		threads[3]=new Thread(new ReadTask(obj), "Read1");
		for(Thread t:threads)
			t.start();
		Thread monitor=new Thread(new Monitor(threads));
		monitor.start();
	}

}
