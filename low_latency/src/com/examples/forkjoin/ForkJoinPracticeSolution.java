package com.examples.forkjoin;

import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.TimeUnit;

public class ForkJoinPracticeSolution {

	private static final int ITERATIONS = 10000;
	public static final int SPLIT_SIZE = 10;

	private ForkJoinPool executor = new ForkJoinPool();

	public static void main(String[] args) {
		ForkJoinPracticeSolution app = new ForkJoinPracticeSolution();
		app.start();
	}

	public void start() {
		int[] values = new int[ITERATIONS];
		try {
			//
			// Fill array
			//
			ForkJoinTask<Void> fillingAction = new FillingAction(values, 0, ITERATIONS);
			// async execution
			executor.execute(fillingAction);
			printStatus(fillingAction);

			//
			// Count array
			//

			ForkJoinTask<Integer> counterTask = new CounterTask(values, 0, ITERATIONS);
			// async execution
			executor.execute(counterTask);
			printStatus(counterTask);
			// get waits for the result to be ready
			System.out.println("Final result: " + counterTask.get());
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

class FillingAction extends RecursiveAction {

	private static final long serialVersionUID = -3334015437582652859L;

	private final int[] values;
	private final int start;
	private final int end;

	public FillingAction(int[] values, int start, int end) {
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		if (end - start > ForkJoinPracticeSolution.SPLIT_SIZE) {
			Collection<FillingAction> actions = split(start, end);
			ForkJoinTask.invokeAll(actions);
			// System.out.printf("Task: Pending tasks: %s\n",
			// getQueuedTaskCount());
		} else {
			resolve(start, end);
		}
	}

	private Collection<FillingAction> split(int start, int end) {
		int middle = (start + end) / 2;
		FillingAction action1 = new FillingAction(values, start, middle + 1);
		// System.out.println(action1);
		FillingAction action2 = new FillingAction(values, middle + 1, end);
		// System.out.println(action2);
		return Arrays.asList(action1, action2);
	}

	private void resolve(int start, int end) {
		for (int i = start; i < end; i++) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			values[i] = 1;
		}
	}

	@Override
	public String toString() {
		return Thread.currentThread().getName(); // " Start:" + this.start +
													// " End:" + this.end;
	}
}

class CounterTask extends RecursiveTask<Integer> {

	private final int[] values;
	private final int start;
	private final int end;

	public CounterTask(int[] values, int start, int end) {
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected Integer compute() {
		Integer result = null;
		if (end - start > ForkJoinPracticeSolution.SPLIT_SIZE) {
			Collection<CounterTask> tasks = split(start, end);
			ForkJoinTask.invokeAll(tasks);
			try {
				result = combine(tasks);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ExecutionException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			result = resolve(start, end);
		}
		return result;
	}

	private Collection<CounterTask> split(int start, int end) {
		int middle = (start + end) / 2;
		CounterTask task1 = new CounterTask(values, start, middle + 1);
		// System.out.println(task1);
		CounterTask task2 = new CounterTask(values, middle + 1, end);
		// System.out.println(task2);
		return Arrays.asList(task1, task2);
	}

	private Integer resolve(int start, int end) {
		int result = 0;
		for (int i = start; i < end; i++) {
			result += values[i];
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	private Integer combine(Collection<CounterTask> tasks) throws InterruptedException, ExecutionException {
		int result = 0;
		for (CounterTask task : tasks) {
			result += task.get().intValue();
		}
		return result;
	}

	@Override
	public String toString() {
		return Thread.currentThread().getName(); // " Start:" + this.start +
													// " End:" + this.end;
	}
}
