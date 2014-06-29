package com.executors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CallableSample {

	private static final int ITERATONS = 10;

	public static void main(String args[]) throws ExecutionException {

		List<Callable<String>> callables = new ArrayList<Callable<String>>(ITERATONS);
		for (int i = 0; i < ITERATONS; i++) {
			callables.add(new ToUpperCaseCallable("task name: " + i));
		}

		ExecutorService executor = Executors.newFixedThreadPool(5);
		try {
			System.out.println(new Date() + " Waining for ALL tasks to finish");
			List<Future<String>> futures = executor.invokeAll(callables);
			System.out.println(new Date() + " tasks have finished");
			for (Future<String> future : futures) {
				String result = future.get();
				System.out.println(result);
			}

		} catch (InterruptedException | ExecutionException e1) {
			// TODO manage exception
		}
		System.out.println("---------------------------------------");
		try {
			System.out.println(new Date() + " Waining for any tasks to finish");
			String result = executor.invokeAny(callables);
			System.out.println(new Date() + " task have finished");
			System.out.println(new Date() + " " + result);
		} catch (InterruptedException | ExecutionException e1) {
			// TODO manage exception
		}

		// shut down the executor service now
		executor.shutdown();
	}
}

class ToUpperCaseCallable implements Callable<String> {

	private String taskName;

	public ToUpperCaseCallable(String taskName) {
		this.taskName = taskName;
	}

	@Override
	public String call() throws Exception {
		TimeUnit.SECONDS.sleep(5);
		return new Date() + " " + Thread.currentThread().getName() + " " + taskName.toUpperCase();
	}
}