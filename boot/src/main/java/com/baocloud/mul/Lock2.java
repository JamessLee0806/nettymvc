package com.baocloud.mul;

import java.util.Vector;

public class Lock2 {
	public static class Consumer implements Runnable{
        private Vector<String> vec;
        public Consumer(Vector<String> v){
        	this.vec=v;
        }
		@Override
		public void run() {
			while (true) {
				synchronized (vec) {
					if(vec.isEmpty()){
						try {
							vec.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					String str=vec.get(0);
					System.out.println("get "+str);
					vec.clear();
					vec.notify();
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
				}
			}
		}
		
	}
	
	public static class Producter implements Runnable{
		private Vector<String> vec;
		public Producter(Vector<String> v){
			this.vec=v;
		}

		@Override
		public void run() {
			while(true){
				synchronized (vec) {
					if(!vec.isEmpty()){
						try {
							vec.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					vec.addElement("str");
					System.out.println("Producter:obj are ready");
					vec.notify();
					try {
						Thread.sleep(500L);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
			
		}
		
	}
	
	public static void main(String[] args) {
		Vector<String> v=new Vector<>();
		Thread t=new Thread(new Producter(v), "P");
		Thread t2=new Thread(new Consumer(v),"C");
		t.start();
		t2.start();
	}

}
