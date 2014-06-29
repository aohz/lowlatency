package com.solution;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class Practice5Solution {

	private static final String[] STUDENTS = new String[] { "ana", "juan",
			"luis" };

	public static void main(String args[]) throws InterruptedException {
		CountDownLatch finishExam = new CountDownLatch(STUDENTS.length);

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Teacher(finishExam));
		for (int i = 0; i < STUDENTS.length; ++i) {
			// create and start threads
			executor.execute(new Student(finishExam, STUDENTS[i]));
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("End ");
	}

}

class Student implements Runnable {

	private final CountDownLatch finishExam;
	private final String name;

	Student(CountDownLatch finishExam, String name) {
		this.finishExam = finishExam;
		this.name = name;
	}

	@Override
	public void run() {
		System.out.println(name + " is ready to start the exam");
		doWork();
		finishExam.countDown();
	}

	void doWork() {
		// Do something
		System.out.println(name + " has finish");
	}
}

class Teacher implements Runnable {

	private final CountDownLatch finishExam;

	public Teacher(CountDownLatch finishExam) {
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
