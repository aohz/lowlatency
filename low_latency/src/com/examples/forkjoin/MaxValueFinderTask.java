package com.examples.forkjoin;

import java.util.Random;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;

public class MaxValueFinderTask extends RecursiveTask<Integer> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private static final int SEQUENTIAL_THRESHOLD = 5;

	private final int[] data;
	private final int start;
	private final int end;

	public MaxValueFinderTask(int[] data, int start, int end) {
		this.data = data;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		final int length = end - start;
		if (length > SEQUENTIAL_THRESHOLD) {
			final int split = length / 2;
			final MaxValueFinderTask left = new MaxValueFinderTask(data, start, start + split);
			final MaxValueFinderTask right = new MaxValueFinderTask(data, start + split, end);
			left.fork();
			return Math.max(right.compute(), left.join());
		} else {
			return computeDirectly();
		}
	}

	private Integer computeDirectly() {
		System.out.println(Thread.currentThread().getName() + " computing: " + start + " to " + end);
		int max = Integer.MIN_VALUE;
		for (int i = start; i < end; i++) {
			if (data[i] > max) {
				max = data[i];
			}
		}
		return max;
	}
	

	public MaxValueFinderTask(int[] data) {
		this(data, 0, data.length);
	}

	public static void main(String[] args) {
		// create a random data set
		final int[] data = new int[1000];
		final Random random = new Random();
		for (int i = 0; i < data.length; i++) {
			data[i] = random.nextInt(100);
		}

		final ForkJoinPool pool = new ForkJoinPool(4);
		final MaxValueFinderTask finder = new MaxValueFinderTask(data);
		System.out.println(pool.invoke(finder));

	}
}
