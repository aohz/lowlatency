package com.examples.lock;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class LockConditionsProdCons {
	private static final int ITERATIONS = 10;

	private String message;

	private boolean empty = true;

	private Lock lock = new ReentrantLock();
	private Condition notFull = lock.newCondition();
	private Condition notEmpty = lock.newCondition();

	public String take() {
		lock.lock();
		try {
			while (empty) {
				try {
					notFull.await();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Taking " + message);
			// Toggle status.
			empty = true;
			// Notify producer that status has changed.
			notEmpty.signal();
			return message;
		} finally {
			lock.unlock();
		}
	}

	public synchronized void put(String message) {
		lock.lock();
		try {
			while (!empty) {
				try {
					notEmpty.await();
				} catch (InterruptedException e) {
				}
			}
			// Toggle status.
			empty = false;
			// Store message.
			this.message = message;
			System.out.println("Putting " + message);
			// Notify consumer that status has changed.
			notFull.signal();
		} finally {
			lock.unlock();
		}
	}

	public static void main(String[] args) throws InterruptedException {
		final LockConditionsProdCons messageProdCon = new LockConditionsProdCons();

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++)
					messageProdCon.put("Message " + i);

			}

		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++)
					messageProdCon.take();
			}

		});

		t1.start();
		t2.start();

		t1.join();
		t2.join();
	}

}