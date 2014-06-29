package com.examples.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;

class AtomicIntegerFieldUpdaterCounter {

	private static final AtomicIntegerFieldUpdater<Details> updater = AtomicIntegerFieldUpdater
			.newUpdater(Details.class, "value");

	public static  int getNextValue(Details details) {
		return updater.incrementAndGet(details);
	}

	public static int getPreviousValue(Details details) {
		return updater.decrementAndGet(details);
	}

	public static void setValue(Details details, int newValue) {
		updater.set(details, newValue);
	}

}

class Details {
	// if not volatile, an IllegalArgumentException("Must be volatile type") is thrown
	volatile int value;
}

public class AtomicIntegerFieldUpdaterCounterTest {

	public static void main(String args[]) throws InterruptedException {

		final Details details = new Details();
				
		Runnable ct = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 10; i++) {
					AtomicIntegerFieldUpdaterCounter.getNextValue(details);
					AtomicIntegerFieldUpdaterCounter.getPreviousValue(details);
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		};

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(ct);
		executor.execute(ct);
		executor.execute(ct);
		executor.execute(ct);
		executor.execute(ct);
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		System.out.println(details.value);
	}
}
