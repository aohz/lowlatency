package com.practice;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Practice8 {

	static final int ITERATIONS = 5;

	public static void main(String[] args) throws InterruptedException {

		ConsumerPS consumer = new ConsumerPS();
		ProducerPS producer = new ProducerPS();

		ExecutorService executorService = Executors.newCachedThreadPool();
		executorService.execute(consumer);
		executorService.execute(consumer);
		executorService.execute(producer);

		executorService.shutdown();
	}
}

class ConsumerPS implements Runnable {

	@Override
	public void run() {
		try {
			while (true) {
				System.out.println(Thread.currentThread().getName() + " "
						+ new Date() + " Consumed: ");
				Thread.sleep(5000);
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " "
					+ new Date() + " Stopped while waiting");
		}
	}

}

class ProducerPS implements Runnable {

	@Override
	public void run() {
		for (int i = 0; i < Practice8.ITERATIONS; i++) {

		}

	}

}
