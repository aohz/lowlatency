package com.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class ForkJoinSyncArrayInitilization {

	private static final int ITERATIONS = 1000;
	public static final int SPLIT_SIZE = 50;
	private int[] values = new int[ITERATIONS];
	
	private ForkJoinPool executor = new ForkJoinPool();

	public static void main(String[] args) {
		ForkJoinSyncArrayInitilization app = new ForkJoinSyncArrayInitilization();
		app.start();
	}

	public void start() {		
		try {
			ForkJoinTask<Void> recAction = new Action(values, 0, ITERATIONS);
			executor.execute(recAction);
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);
		} catch (InterruptedException e) {
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
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());

	}
}

class Action extends RecursiveAction {

	private static final long serialVersionUID = -3334015437582652859L;

	private final int[] values;
	private final int start;
	private final int end;

	public Action(int[] values, int start, int end) {
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		if (end - start > ForkJoinSyncArrayInitilization.SPLIT_SIZE) {
			int middle = (start + end) / 2;
			Action action1 = new Action(values, start, middle + 1);
			Action action2 = new Action(values, middle + 1, end);
			ForkJoinTask.invokeAll(action1, action2);
		} else {
			resolve(start, end);
		}
	}

	private void resolve(int start, int end) {
		try {
			Thread.sleep(10000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		for (int i = start; i < end; i++) {
			values[i] = 1;
		}
	}

	@Override
	public String toString() {
		return Thread.currentThread().getName(); // " Start:" + this.start +
													// " End:" + this.end;
	}
}
