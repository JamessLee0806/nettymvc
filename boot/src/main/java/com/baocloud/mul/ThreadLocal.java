package com.baocloud.mul;

public class ThreadLocal {
	static class Runner implements Runnable {
		private long i;
		private java.lang.ThreadLocal<Long> locals;

		public Runner(long i, java.lang.ThreadLocal<Long> local) {
			this.i = i;
			this.locals = local;
		}

		@Override
		public void run() {
			for (int j = 0; j <= 100; j++) {
				i++;
			}
			for (int j = 0; j <= 100; j++) {
				locals.set(Long.valueOf(j));
			}
		}

		public void getValue() {
			System.out.println("i=" + i);
			System.out.println("locals=" + locals.get());
		}

	}

	public static void main(String[] args) {
		Runner r = new Runner(0, new java.lang.ThreadLocal<Long>());
		for (int i = 0; i < 5; i++) {
			Thread t = new Thread(r, String.valueOf(i));
			t.start();
		}
		r.getValue();
	}

}
