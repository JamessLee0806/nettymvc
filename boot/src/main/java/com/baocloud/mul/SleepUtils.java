package com.baocloud.mul;

import java.util.concurrent.TimeUnit;

public class SleepUtils {
	
	public static void second(int value){
		try {
			TimeUnit.SECONDS.sleep(value);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
