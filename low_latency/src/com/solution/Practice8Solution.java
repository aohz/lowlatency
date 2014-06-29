package com.solution;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.SynchronousQueue;

public class Practice8Solution {

	static final int ITERATIONS = 5;

	public static void main(String[] args) throws InterruptedException {

		CountDownLatch countDownLatch = new CountDownLatch(ITERATIONS);

		BlockingQueue<Integer> queue = new SynchronousQueue<Integer>();

		ConsumerPS consumer = new ConsumerPS(queue, countDownLatch);
		ProducerPS producer = new ProducerPS(queue);

		ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(consumer);
		executorService.execute(consumer);
		executorService.execute(producer);

		countDownLatch.await();
		executorService.shutdownNow();		
	}
}

class ConsumerPS implements Runnable {

	private CountDownLatch countDownLatch;
	private BlockingQueue<Integer> queue;

	public ConsumerPS(BlockingQueue<Integer> queue,
			CountDownLatch countDownLatch) {
		this.queue = queue;
		this.countDownLatch = countDownLatch;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				Integer item = queue.take();
				System.out.println(Thread.currentThread().getName() + " "
						+ new Date() + " Consumed: " + item);
				Thread.sleep(5000);
				countDownLatch.countDown();
			}
			System.out.println(Thread.currentThread().getName() + " "
					+ new Date() + " Stopped");
		} catch (InterruptedException e) {
			// new CancellationException(Thread.currentThread().getName() + " "	+ new Date() + " Stopped while waiting");
			System.out.println(Thread.currentThread().getName() + " "	+ new Date() + " Stopped while waiting");
		}
	}

}

class ProducerPS implements Runnable {

	public ProducerPS(BlockingQueue<Integer> queue) {
		this.queue = queue;
	}

	private BlockingQueue<Integer> queue;

	@Override
	public void run() {
		try {
			for (int i = 0; i < Practice8Solution.ITERATIONS; i++) {
				System.out.println(Thread.currentThread().getName() + " "
						+ new Date() + " wait to produce: " + i);
				queue.put(i);
				System.out.println(Thread.currentThread().getName() + " "
						+ new Date() + " produce: " + i);
			}
		} catch (InterruptedException e) {

		}
	}

}
