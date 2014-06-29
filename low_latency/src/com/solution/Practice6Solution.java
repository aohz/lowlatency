package com.solution;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Practice6Solution {

	private static final String[] STUDENTS = new String[] { "ana", "juan",
			"luis" };

	public static void main(String args[]) throws InterruptedException {
		CyclicBarrier startExam = new CyclicBarrier(STUDENTS.length);
		CountDownLatch finishExam = new CountDownLatch(STUDENTS.length);

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Teacher(finishExam));
		for (int i = 0; i < STUDENTS.length; ++i) {
			// create and start threads
			executor.execute(new Student2(startExam, finishExam, STUDENTS[i]));
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("End ");
	}

}

class Student2 implements Runnable {

	private final CyclicBarrier startExam;
	private final CountDownLatch finishExam;
	private final String name;

	public Student2(CyclicBarrier startExam, CountDownLatch finishExam,
			String name) {
		this.startExam = startExam;
		this.finishExam = finishExam;
		this.name = name;
	}

	@Override
	public void run() {
		System.out.println(name + " is ready to start the exam");
		try {
			startExam.await();
			doWork();
			finishExam.countDown();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	void doWork() {
		try {
			// Do something
			Thread.sleep(10);
			System.out.println(name + " has finish");
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}

class Teacher2 implements Runnable {

	private final CountDownLatch finishExam;

	public Teacher2(CountDownLatch finishExam) {
		this.finishExam = finishExam;
	}

	@Override
	public void run() {
		try {
			finishExam.await();
			doWork();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	void doWork() {
		System.out.println("Teacher can leave");
	}
}
