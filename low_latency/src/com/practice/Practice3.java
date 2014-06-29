package com.practice;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Practice3 {

	private static final int ITERATIONS = 10;

	public static void main(String[] args) throws InterruptedException {
		final StorePractice3 store = new StorePractice3();

		Thread producer = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					store.updateA("Message A: " + i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}
				}
			}

		}, "Producer");

		Thread consumer = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					store.updateB("Message B: " + i);
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {

					}
				}
			}

		}, "Consumer");

		producer.start();
		consumer.start();

		producer.join();
		consumer.join();
		System.out.println("End");
	}

}

class StorePractice3 {

	private String messageA;
	private String messageB;

	private Lock lockA = new ReentrantLock();
	private Lock lockB = new ReentrantLock();

	public void updateA(String msg) {
		lockA.lock();
		lockB.lock();
		try {
			messageA = msg;
			System.out.println(messageA);
		} finally {
			lockB.unlock();
			lockA.unlock();
		}
	}

	public void updateB(String msg) {
		lockB.lock();
		lockA.lock();
		try {
			messageB = msg;
			System.out.println(messageB);
		} finally {
			lockA.unlock();
			lockB.unlock();
		}
	}

}
