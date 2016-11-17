package com.baocloud.mul.pattern;

import com.baocloud.mul.SleepUtils;

public class Task implements Runnable {

	@Override
	public void run() {
		while (true) {
			SingleA a = SingleA.getInstace();
			SleepUtils.second(5);
			break;
		}

	}

}
