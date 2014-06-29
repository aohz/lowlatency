package com.solution;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class Practice10Solution {

	public static void main(String[] args) throws InterruptedException {
		long start = System.nanoTime();
		int[] values = new int[100];

		ForkJoinPool executor = new ForkJoinPool(2);
		executor.execute(new InitializationTask(values, 0, values.length));
		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);

		int result = 0;
		for (int i = 0; i < values.length; i++) {
			result += values[i];
		}
		System.out.println(result);
		long end = System.nanoTime();
		System.out.println(end - start);
	}
}

class InitializationTask extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SPLIT_SIZE = 50;

	private int[] values;
	private int start;
	private int end;

	public InitializationTask(int[] values, int start, int end) {
		super();
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		if (end - start > SPLIT_SIZE) {
			int middle = (start + end) / 2;
			InitializationTask action1 = new InitializationTask(values, start,
					middle + 1);
			InitializationTask action2 = new InitializationTask(values,
					middle + 1, end);
			ForkJoinTask.invokeAll(action1, action2);
		} else {
			resolve(start, end);
		}
	}

	private void resolve(int start, int end) {
		try {
			for (int i = start; i < end; i++) {
				Thread.sleep(1);
				values[i] = 1;
			}
			print();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void print() {
		System.out.println(this);
	}

	@Override
	public String toString() {
		return Thread.currentThread().getName() + " Task(" + start + "-" + end
				+ ")" + " done: " + this.isDone() + " completed Normally: "
				+ this.isCompletedNormally();

	}
}
