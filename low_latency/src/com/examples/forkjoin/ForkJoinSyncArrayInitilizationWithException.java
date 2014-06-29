package com.forkjoin;

import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.TimeUnit;

public class ForkJoinSyncArrayInitilizationWithException {

	private static final int ITERATIONS = 100;
	public static final int SPLIT_SIZE = 50;
	private int[] values = new int[ITERATIONS];

	private ForkJoinPool executor = new ForkJoinPool();

	public static void main(String[] args) {
		ForkJoinSyncArrayInitilizationWithException app = new ForkJoinSyncArrayInitilizationWithException();
		app.start();
	}

	public void start() {
		try {
			ForkJoinTask<Void> task = new ActionWithExp(values, 0,
					ITERATIONS);
			executor.execute(task);
			executor.shutdown();
			executor.awaitTermination(1, TimeUnit.DAYS);

			if (task.isCompletedAbnormally()) {
				System.out.println("error");
			}
			
			if (task.isCompletedAbnormally()) {
				System.out.println("error");
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void printStatus(ForkJoinTask<?> task) {
		do {
			System.out.printf("Main: Pool Size: %d\n", executor.getPoolSize());
			System.out.printf("Main: Target Parallelism: %d\n",
					executor.getParallelism());
			System.out.printf("Main: Threads executing a task: %d\n",
					executor.getActiveThreadCount());
			System.out.printf("Main: non blocked threads: %d\n",
					executor.getRunningThreadCount());

			System.out.printf("Main: Steal Count: %d\n",
					executor.getStealCount());

			System.out.printf("Main: Submited tasks not started: %d\n",
					executor.getQueuedSubmissionCount());

			System.out.printf("Main: Submited and started tasks: %d\n",
					executor.getQueuedTaskCount());

			try {
				TimeUnit.SECONDS.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} while (!task.isDone());

	}
}

class ActionWithExp extends RecursiveAction {

	private static final long serialVersionUID = -3334015437582652859L;

	private final int[] values;
	private final int start;
	private final int end;

	public ActionWithExp(int[] values, int start, int end) {
		this.values = values;
		this.start = start;
		this.end = end;
	}

	@Override
	protected void compute() {
		try {
			if (end - start > ForkJoinSyncArrayInitilizationWithException.SPLIT_SIZE) {
				int middle = (start + end) / 2;
				ActionWithExp action1 = new ActionWithExp(values, start,
						middle + 1);
				ActionWithExp action2 = new ActionWithExp(values, middle + 1,
						end);
				try {
					ForkJoinTask.invokeAll(action1, action2);
//				} catch (Exception e) {
//					
				}
				finally{
					System.out.println(action1 + "     " + action2);
				}
			} else {
				if (start > 20) {
					throw new RuntimeException();
				}
				resolve(start, end);
			}
		} finally {
			// System.out.println(this.toString());
		}
	}

	private void resolve(int start, int end) {		 
		 Thread.yield();		 
		for (int i = start; i < end; i++) {
			values[i] = 1;
		}
		//System.out.println("resolved: "+this.toString());
	}

	@Override
	public String toString() {
		return Thread.currentThread().getName() + " Task(" + start + "-" + end + ")" + " done: " + this.isDone() + " completed Normally: "
				+ this.isCompletedNormally();

	}
}
