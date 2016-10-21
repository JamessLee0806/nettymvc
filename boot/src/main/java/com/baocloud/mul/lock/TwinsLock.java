package com.baocloud.mul.lock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;

public class TwinsLock implements Lock {
	private final Sync sync = new Sync(2);

	@SuppressWarnings("serial")
	private static class Sync extends AbstractQueuedSynchronizer {
		public Sync(int state) {
			if (state <= 0)
				throw new IllegalArgumentException("无效的参数");
			setState(state);
		}

		public int tryAcquireShared(int reduceCount) {
			for (;;) {
				int current = getState();
				int nowCount = current - reduceCount;
				if (nowCount < 0 || compareAndSetState(current, nowCount)) {
					return nowCount;
				}
			}
		}

		public boolean tryReleaseShared(int releaseCount) {
			for (;;) {
				int current = getState();
				int nowCount = current + releaseCount;
				if (compareAndSetState(current, nowCount)) {
					return true;
				}
			}
		}

		final ConditionObject newCodition() {
			return new ConditionObject();
		}

	}

	@Override
	public void lock() {
		sync.acquireShared(1);
	}

	@Override
	public void lockInterruptibly() throws InterruptedException {
		sync.acquireSharedInterruptibly(1);
	}

	@Override
	public boolean tryLock() {
		return sync.tryAcquireShared(1)>0;
	}

	@Override
	public boolean tryLock(long time, TimeUnit unit) throws InterruptedException {
		return  sync.tryAcquireSharedNanos(1, unit.toNanos(time));
	}

	@Override
	public void unlock() {
		sync.releaseShared(1);
	}

	@Override
	public Condition newCondition() {
		return sync.newCodition();
	}

}
