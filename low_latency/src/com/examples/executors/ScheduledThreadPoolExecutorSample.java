package com.examples.executors;

import java.util.Date;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledThreadPoolExecutorSample {

	public static void main(String[] args) throws InterruptedException {
		ScheduledExecutorService scheduledThreadPool = Executors
				.newScheduledThreadPool(5);

		// schedule to run after sometime
		System.out.println("Current Time = " + new Date());
		int delay = 0;
		for (int i = 0; i < 3; i++) {
			delay = 1 + delay;
			WorkerThread worker = new WorkerThread("do heavy processing");
			scheduledThreadPool.schedule(worker, delay, TimeUnit.SECONDS);
		}
		
		scheduledThreadPool.shutdown();
		while (!scheduledThreadPool.isTerminated()) {
			// wait for all tasks to finish
		}
		System.out.println("Finished all threads");
	}

}