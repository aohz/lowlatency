package com.solution;

import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class Practice9Solution {

	static final int ITERATIONS = 20;

	public static void main(String[] args) throws InterruptedException {

		BlockingQueue<Task> queue = new PriorityBlockingQueue<Task>(ITERATIONS);

		Employee consumer = new Employee(queue);
		Boss producer = new Boss(queue);

		ExecutorService executorService = Executors.newFixedThreadPool(3);		
		executorService.execute(producer);
		
		executorService.execute(consumer);
		executorService.execute(consumer);

		executorService.shutdown();
	}
}

class Task implements Comparable<Task> {

	private int priotiry;

	public Task(int priotiry) {
		this.priotiry = priotiry;
	}

	@Override
	public int compareTo(Task o) {
		return priotiry;
	}

	@Override
	public String toString() {
		return "Priority " + priotiry;
	}
}

class Employee implements Runnable {

	private BlockingQueue<Task> queue;

	public Employee(BlockingQueue<Task> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			while (!Thread.currentThread().isInterrupted()) {
				System.out.println(queue.take());
				TimeUnit.SECONDS.sleep(2);
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " "
					+ new Date() + " Stopped while waiting");
		}
	}

}

class Boss implements Runnable {

	private BlockingQueue<Task> queue;

	public Boss(BlockingQueue<Task> queue) {
		this.queue = queue;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < Practice9Solution.ITERATIONS; i++) {
				queue.put(new Task(ThreadLocalRandom.current().nextInt(10)));
				TimeUnit.SECONDS.sleep(1);
			}
		} catch (InterruptedException e) {
			System.out.println(Thread.currentThread().getName() + " "
					+ new Date() + " Stopped while waiting");
		}
	}

}
