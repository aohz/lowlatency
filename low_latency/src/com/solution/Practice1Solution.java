package com.solution;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Practice1Solution {

	private static final int ITERATIONS = 10;

	public static void main(String[] args) throws InterruptedException {
		final StorePractice1 store = new StorePractice1();

		Thread producer = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					store.put("Message " + i);
				}

			}

		}, "Producer");

		Thread consumer = new Thread(new Runnable() {

			@Override
			public void run() {
				try {
					for (int i = 0; i < ITERATIONS; i++) {
						store.take();
					}
				} catch (InterruptedException e) {
					System.out.println("Interrupted");					
				}
			}

		}, "Consumer");

		producer.start();
		consumer.start();

		producer.join();
		consumer.join(100);

		consumer.interrupt();
	}

}

class StorePractice1 {

	private String message;

	private boolean empty = true;

	private Lock lock = new ReentrantLock();
	private Condition notFull = lock.newCondition();

	public String take() throws InterruptedException {
		lock.lock();
		try {
			while (empty) {
				notFull.await();
			}
			System.out.println("Taking " + message);
			// Toggle status.
			empty = true;
			return message;
		} finally {
			lock.unlock();
		}
	}

	public void put(String message) {
		lock.lock();
		try {
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
}
