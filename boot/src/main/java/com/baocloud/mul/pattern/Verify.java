package com.baocloud.mul.pattern;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Verify {
	public static void main(String[] args) {
		ExecutorService service=Executors.newFixedThreadPool(10);
		for(int i=0;i<10;i++){
			service.execute(new Task());
		}
		service.shutdown();

	}
}
