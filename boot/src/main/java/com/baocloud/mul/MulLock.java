package com.baocloud.mul;

public class MulLock {
	public static class Task implements Runnable {
		private Integer obj;
		private Integer orgObj;

		public Task(Integer o) {
			this.obj = o;
			orgObj = o;
		}

		@Override
		public void run() {
			synchronized (obj) {
				if (orgObj.equals(obj)) {
					System.out.println("Thread " + Thread.currentThread().getName() + " get lock");
					obj = 2;
					try {
						Thread.sleep(200L);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				   obj.notify();
				} else {
					System.out.println("Thread " + Thread.currentThread().getName() + " cant get lock");
					long begin=System.currentTimeMillis();
					try {
						
						obj.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					long end=System.currentTimeMillis();
					System.out.println("Thread "+Thread.currentThread().getName()+" wait "+(end-begin));
				}
			}
		}

	}

	private  static Integer o = new Integer(1);

	public static void main(String[] args) throws InterruptedException {
//		Task t = new Task(o);
//		Task tt = new Task(o);
//		Thread t1 = new Thread(t, "T1");
//		Thread t2 = new Thread(tt, "T2");
//		t1.start();
//		t2.start();
		Integer i1=new Integer(0);
		Integer i2=i1;
		i1=2;
		System.out.println(i1);
		System.out.println(i2);

	}
}
