package com.practice;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Practice9 {

	static final int ITERATIONS = 20;

	public static void main(String[] args) throws InterruptedException {

		Employee consumer = new Employee();
		Boss producer = new Boss();

		ExecutorService executorService = Executors.newFixedThreadPool(3);
		executorService.execute(producer);

		executorService.execute(consumer);
		executorService.execute(consumer);

		executorService.shutdown();
	}
}

class Task {

}

class Employee implements Runnable {

	public Employee() {

	}

	@Override
	public void run() {

	}

}

class Boss implements Runnable {

	@Override
	public void run() {

	}

}
