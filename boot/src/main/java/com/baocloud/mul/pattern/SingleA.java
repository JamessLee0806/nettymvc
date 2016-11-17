package com.baocloud.mul.pattern;

public class SingleA {
	private SingleA() {

	}

	private static volatile SingleA instance;

	public static SingleA getInstace() {
		if (instance == null) {
			synchronized (SingleA.class) {
				if (instance == null) {
					System.out.println("Thread:" + Thread.currentThread().getName() + "Create a Object");
					instance = new SingleA();
				}
				
			}

		}
		return instance;
	}
}
