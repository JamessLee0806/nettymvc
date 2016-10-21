package com.baocloud.mul.lock;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Cache {
	final static Map<Object, Object> map = new HashMap<>();
	final static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock();
	final static Lock readLock = rwLock.readLock();
	final static Lock writeLock = rwLock.writeLock();
	volatile static boolean update = true;

	public Object get(Object key) {
		readLock.lock();
		try {
			return map.get(key);
		} finally {
			readLock.unlock();
		}
	}

	public void put(Object key, Object value) {
		writeLock.lock();
		try {
			map.put(key, value);
		} finally {
			writeLock.unlock();
		}
	}

	public void clear() {
		writeLock.lock();
		try {
			map.clear();
		} finally {
			writeLock.unlock();
		}
	}

	public void processData() {
		readLock.lock();
		if (!update) {
			readLock.unlock();
			writeLock.lock();
			if (!update) {
				update = true;
			}
			try {
				readLock.lock();
			} finally {
				writeLock.unlock();
			}

		}
		try {

		} finally {
			readLock.unlock();
		}
	}

}
