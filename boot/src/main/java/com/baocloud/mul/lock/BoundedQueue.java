package com.baocloud.mul.lock;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.baocloud.mul.SleepUtils;

public class BoundedQueue<T> {
	private Object[] items;
	private int addInd, removeInd, count;
	private Lock lock = new ReentrantLock();
	Condition notEmpty = lock.newCondition();
	Condition notFull = lock.newCondition();

	public BoundedQueue(int size) {
		items = new Object[size];
	}

	public void add(T t) throws InterruptedException {
		lock.lock();
		try {
			while (count == items.length) {
				notFull.await();
			}

			items[addInd] = t;
			addInd++;
			if (addInd == items.length)
				addInd = 0;
			++count;
			notEmpty.signal();

		} finally {
			lock.unlock();
		}
	}

	public T romove() throws InterruptedException {
		lock.lock();
		try {
			while (count == 0) {
				notEmpty.await();
			}
			@SuppressWarnings("unchecked")
			T x = (T) items[removeInd];
			removeInd++;
			if (removeInd == items.length)
				removeInd = 0;
			count--;
			notFull.signal();
			return x;
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) {
		int size = 10;
		BoundedQueue<String> queue = new BoundedQueue<>(size);
		ExecutorService producers = Executors.newFixedThreadPool(size);
		ExecutorService consumers = Executors.newFixedThreadPool(size);
		for (int i = 0; i < size; i++) {
			Producer p = new Producer(queue);
			Consumer c = new Consumer(queue);
			producers.execute(p);
			consumers.execute(c);
		}

	}
}

class Producer implements Runnable {
	private BoundedQueue<String> queue;
	Random random = new Random(100);

	public Producer(BoundedQueue<String> q) {
		this.queue = q;
	}

	@Override
	public void run() {
		while (true) {
			String value = Thread.currentThread().getName();
			System.out.println("Thread " + Thread.currentThread().getName() + " generate value  " + value);
			try {
				queue.add(value);
			} catch (InterruptedException e) {
			}
			SleepUtils.second(1);
		}

	}

}

class Consumer implements Runnable {
	private BoundedQueue<String> queue;

	public Consumer(BoundedQueue<String> q) {
		this.queue = q;
	}

	@Override
	public void run() {
		while (true) {
			try {
				String value = queue.romove();
				System.out.println("Thread " + Thread.currentThread().getName() + " receive " + value);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			SleepUtils.second(1);

		}

	}

}
