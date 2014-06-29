package com.lock;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockReentrantLock {

	private static final int ITERATIONS = 100;

	private int value;
	private Lock lock = new ReentrantLock();

	public void increment() {
		try {
			while (true) {
				if (lock.tryLock()) {
					value++;
					break;
				} else {
					System.out.println(Thread.currentThread().getName()
							+ " waiting for lock");
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public void decrement() {
		try {
			while (true) {
				if (lock.tryLock()) {
					value--;
					break;
				} else {
					System.out.println(Thread.currentThread().getName()
							+ " waiting for lock");
				}
			}
		} finally {
			lock.unlock();
		}
	}

	public int getValue() {
		return value;
	}

	public static void main(String args[]) throws InterruptedException {
		final LockReentrantLock counter = new LockReentrantLock();
		Runnable incrementer = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					counter.increment();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Runnable decrementer = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					counter.decrement();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread t1 = new Thread(incrementer);
		Thread t2 = new Thread(decrementer);
		t1.start();
		t2.start();

		t1.join();
		t2.join();

		System.out.println(counter.getValue());
	}
}
