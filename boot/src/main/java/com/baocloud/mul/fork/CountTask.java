package com.baocloud.mul.fork;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

@SuppressWarnings({ "serial" })
public class CountTask extends RecursiveTask<Long> {
	private int start;
	private int end;
	private int throld = 2;

	public CountTask(int start, int end) {
		this.start = start;
		this.end = end;
	}

	@Override
	protected Long compute() {
		long result = 0;
		if (end - start <= throld) {
			for (int i = start; i <= end; i++) {
				result += i;
			}
		} else {
			int middle = (start + end) / 2;
			CountTask leftTask = new CountTask(start, middle);
			CountTask rightTask = new CountTask(middle + 1, end);
			leftTask.fork();
			rightTask.fork();
			Long leftR = leftTask.join();
			Long rightR = rightTask.join();
			result = leftR + rightR;

		}
		return result;
	}

	public static void main(String[] args) throws InterruptedException, ExecutionException {
		int total=999999999;
		CountTask t = new CountTask(1,total );
		ForkJoinPool pool = new ForkJoinPool(Runtime.getRuntime().availableProcessors());
		Long begin=System.currentTimeMillis();
		Future<Long> result = pool.submit(t);
		System.out.println(result.get());
		Long end=System.currentTimeMillis();
		System.out.println("use Time:"+(end-begin));
		long r=0;
		begin=System.currentTimeMillis();
		for(int i=1;i<=total;i++){
			r+=i;
		}
		System.out.println("ttt=="+r);
		end=System.currentTimeMillis();
		System.out.println("use Time:"+(end-begin));
	}

}
