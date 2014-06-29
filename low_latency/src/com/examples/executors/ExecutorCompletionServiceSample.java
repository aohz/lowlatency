package com.examples.executors;

import java.util.concurrent.Callable;
import java.util.concurrent.CompletionService;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorCompletionService;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorCompletionServiceSample {

	public static void main(String args[]) {
		ExecutorService taskExecutor = Executors.newFixedThreadPool(3);
		CompletionService<ECSCalcResult> taskCompletionService = new ExecutorCompletionService<ECSCalcResult>(taskExecutor);

		int submittedTasks = 5;
		for (int i = 0; i < submittedTasks; i++) {
			taskCompletionService.submit(new ECSCallableTask(String.valueOf(i), (i * 10), ((i * 10) + 10)));
			System.out.println("Task " + String.valueOf(i) + "subitted");
		}
		for (int tasksHandled = 0; tasksHandled < submittedTasks; tasksHandled++) {
			try {
				System.out.println("trying to take from Completion service");
				Future<ECSCalcResult> result = taskCompletionService.take();
				System.out.println("result for a task availble in queue.Trying to get()");
				// above call blocks till atleast one task is completed and
				// results availble for it but we dont have to worry which one
				// process the result here by doing result.get()
				ECSCalcResult l = result.get();
				System.out.println("Task " + String.valueOf(tasksHandled) + "Completed - results obtained : " + String.valueOf(l.result));

			} catch (InterruptedException e) {
				// Something went wrong with a task submitted
				System.out.println("Error Interrupted exception");
				e.printStackTrace();
			} catch (ExecutionException e) {
				// Something went wrong with the result
				e.printStackTrace();
				System.out.println("Error get() threw exception");
			}
		}
	}
}

class ECSCallableTask implements Callable<ECSCalcResult> {
	String taskName;
	long input1;
	int input2;

	ECSCallableTask(String name, long v1, int v2) {
		taskName = name;
		input1 = v1;
		input2 = v2;
	}

	public ECSCalcResult call() throws Exception {
		System.out.println(" Task " + taskName + " Started -----");
		for (int i = 0; i < input2; i++) {
			try {
				Thread.sleep(200);
			} catch (InterruptedException e) {
				System.out.println(" Task " + taskName + " Interrupted !! ");
				e.printStackTrace();
			}
			input1 += i;
		}
		System.out.println(" Task " + taskName + " Completed @@@@@@");
		return new ECSCalcResult(input1);
	}
}

class ECSCalcResult {
	long result;

	ECSCalcResult(long l) {
		result = l;
	}
}
