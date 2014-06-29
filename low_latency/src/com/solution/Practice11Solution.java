package com.solution;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

public class Practice11Solution {

	public static void main(String[] args) throws InterruptedException {
		long start = System.nanoTime();
		int[] values = new int[100];

		ForkJoinPool executor = new ForkJoinPool(2);
		executor.invoke(new InitializationTask2(values, 0, values.length));
		System.out.println("-----------------");
		int result = executor.invoke(new SumTask(values, 0, values.length));		
		executor.shutdown();
		
		System.out.println(result);
		long end = System.nanoTime();
		System.out.println(end - start);
	}
}

class InitializationTask2 extends RecursiveAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SPLIT_SIZE = 50;

	private int[] values;
	private int start;
	private int end;

	public InitializationTask2(int[] values, int start, int end) {
		super();
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		if (end - start > SPLIT_SIZE) {
			int middle = (start + end) / 2;
			InitializationTask2 action1 = new InitializationTask2(values,
					start, middle + 1);
			InitializationTask2 action2 = new InitializationTask2(values,
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

	private void print(){
		System.out.println(this);
	}
	
	@Override
	public String toString() {
		return Thread.currentThread().getName() + " Task(" + start + "-" + end
				+ ")" + " done: " + this.isDone() + " completed Normally: "
				+ this.isCompletedNormally();
	}
}

class SumTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SPLIT_SIZE = 50;

	private int[] values;
	private int start;
	private int end;

	public SumTask(int[] values, int start, int end) {
		super();
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int sum = 0;
		if (end - start > SPLIT_SIZE) {
			try {
				int middle = (start + end) / 2;
				SumTask action1 = new SumTask(values, start, middle + 1);
				SumTask action2 = new SumTask(values, middle + 1, end);
				ForkJoinTask.invokeAll(action1, action2);
				sum = action1.get() + action2.get();
				// action1.fork();
				// sum = action2.compute() + action1.join();
			} catch (InterruptedException | ExecutionException e) {
				Thread.currentThread().interrupt();
				e.printStackTrace();
			}
		} else {
			sum = resolve(start, end);
		}
		return sum;
	}

	private int resolve(int start, int end) {
		int sum = 0;
		try {
			for (int i = start; i < end; i++) {
				Thread.sleep(1);
				sum += values[i];
			}
			print();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return sum;
	}
	
	private void print(){
		System.out.println(this);
	}

	@Override
	public String toString() {
		return Thread.currentThread().getName() + " Task(" + start + "-" + end
				+ ")" + " done: " + this.isDone() + " completed Normally: "
				+ this.isCompletedNormally();

	}
}
