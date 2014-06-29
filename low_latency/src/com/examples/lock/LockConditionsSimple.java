package com.examples.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionsSimple {
	private static final int ITERATIONS = 1;

	private boolean message = true;

	private Lock lock = new ReentrantLock();
	private Condition notMessage = lock.newCondition();

	public void notifyMessage() {
		lock.lock();
		try {
			message = true;
			notMessage.signalAll();
		} finally {
			lock.unlock();
		}
	}

	public synchronized void guardedMessage() {
		lock.lock();
		try {
			while (!message) {
				// await(ms),
				// awaitUntil(Date date),
				// awaitUninterruptibly()
				notMessage.awaitUninterruptibly();
			}
			System.out.println(message);
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final LockConditionsSimple messageProdCon = new LockConditionsSimple();

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++)
					messageProdCon.guardedMessage();

			}

		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++)
					messageProdCon.notifyMessage();
			}

		});

		t1.start();
		t2.start();

		t1.join();
		t2.join();
	}

}