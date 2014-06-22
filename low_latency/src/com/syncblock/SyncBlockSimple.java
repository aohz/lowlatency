package com.syncblock;

public class SyncBlockSimple {

	private static final int ITERATIONS = 100;

	private int value;

	public synchronized void increment() {
		value++;
	}

	public synchronized void decrement() {
		value--;
	}

	public int getValue() {
		return value;
	}

	public static void main(String args[]) throws InterruptedException {
		final SyncBlockSimple counter = new SyncBlockSimple();
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
