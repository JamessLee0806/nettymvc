package com.baocloud.mul.lock;

public class verifyTest {
	static boolean fails1() {
		System.out.println("fails1");
		return false;
	}

	static boolean trues() {
		System.out.println("trues");
		return true;
	}

	public static void main(String[] args) {
		System.out.println("===============&&======================");
		boolean flag = trues() && fails1();
		System.out.println("===============&&======================2");
		flag=fails1() && trues();
		System.out.println("==============||========================");
		flag = trues() || fails1();
		System.out.println("==============||========================2");
		flag=fails1()||trues();
		for(int i=1;i<=1;i++){
			System.out.println(i);
		}
	}
}
