package com.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicIntegerArray;

public class AtomicIntegerArraySample {

	static final int SIZE = 100;

	public static void main(String[] args) throws InterruptedException {

		final AtomicIntegerArray values = new AtomicIntegerArray(SIZE);
		
		Runnable producer = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < SIZE; i++) {
					//values.set(i, newValue);
					//values.getAndAdd(i, delta)
					//values.addAndGet(i, delta);
					values.incrementAndGet(i);
				}
			}
		};

		ExecutorService executor = Executors.newFixedThreadPool(5);
		executor.execute(producer);
		executor.execute(producer);
		executor.execute(producer);
		executor.execute(producer);
		executor.execute(producer);

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		int sum = 0;
		for (int i = 0; i < values.length(); i++) {
			//sum += values[i];
			sum += values.get(i);
			
		}
		System.out.println(sum);
	}
}
