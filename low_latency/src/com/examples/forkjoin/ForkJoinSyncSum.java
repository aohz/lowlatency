package com.examples.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinSyncSum {

	private int[] values = new int[ITERATIONS];
	private static final int ITERATIONS = 10000;
	public static final int SPLIT_SIZE = 10;

	private ForkJoinPool executor = new ForkJoinPool();

	public static void main(String[] args) {
		ForkJoinSyncSum app = new ForkJoinSyncSum();
		
		app.start();
	}

	public ForkJoinSyncSum() {
		for (int i = 0; i < values.length; i++) {
			values[i] = 1;
		}
	}

	public void start() {

		try {
			ForkJoinTask<Integer> task = new Task(values, 0, ITERATIONS);
			executor.execute(task);
			printStatus(task);
			System.out.println("Final result: " + task.get());
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	private void printStatus(ForkJoinTask<?> task) {
		do {
			System.out.printf("Main: Pool Size: %d\n", executor.getPoolSize());
			System.out.printf("Main: Target Parallelism: %d\n", executor.getParallelism());
			System.out.printf("Main: Threads executing a task: %d\n", executor.getActiveThreadCount());
			System.out.printf("Main: non blocked threads: %d\n", executor.getRunningThreadCount());

			System.out.printf("Main: Steal Count: %d\n", executor.getStealCount());

			System.out.printf("Main: Submited tasks not started: %d\n", executor.getQueuedSubmissionCount());

			System.out.printf("Main: Submited and started tasks: %d\n", executor.getQueuedTaskCount());

			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());

	}
}

class Task extends RecursiveTask<Integer> {

	private final int[] values;
	private final int start;
	private final int end;

	public Task(int[] values, int start, int end) {
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		int result = 0;
		if (end - start > ForkJoinSyncSum.SPLIT_SIZE) {
			int middle = (start + end) / 2;
			Task task1 = new Task(values, start, middle + 1);
			Task task2 = new Task(values, middle + 1, end);			
			ForkJoinTask.invokeAll(task1, task2);
			// Group
			try {
				result += task1.get() + task2.get();
			} catch (InterruptedException | ExecutionException e) {
				e.printStackTrace();
			}
		} else {
			result = resolve(start, end);
		}
		return result;
	}

	private Integer resolve(int start, int end) {
		try {
			//System.out.println(this);
			Thread.sleep(1);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		int result = 0;
		for (int i = start; i < end; i++) {
			result += values[i];
		}
		return result;
	}

	@Override
	public String toString() {
		return Thread.currentThread().getName(); // " Start:" + this.start +
													// " End:" + this.end;
	}
}
