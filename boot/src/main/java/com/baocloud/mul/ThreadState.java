package com.baocloud.mul;


public class ThreadState {
	public static void main(String[] args) {
		new Thread(new TimeWaiting(), "TimeWaitingThread").start();
		new Thread(new Waiting(), "WaitingThread").start();
		new Thread(new Blocked(), "Blocked-Thread-1").start();
		new Thread(new Blocked(), "Blocked-Thread-2").start();
	}
}

class TimeWaiting implements Runnable {

	@Override
	public void run() {
		while (true) {
			SleepUtils.second(100);
		}
	}

}

class Waiting implements Runnable {

	@Override
	public void run() {
		while (true) {
			synchronized (Waiting.class) {
				try {
					Waiting.class.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

}

class Blocked implements Runnable {

	@Override
	public void run() {
		synchronized (Blocked.class) {
			while (true) {
				SleepUtils.second(100);
			}
		}
	}

}

