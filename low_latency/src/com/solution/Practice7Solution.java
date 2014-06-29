package com.solution;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

public class Practice7Solution {

	private static final String[] STUDENTS = new String[] { "ana", "juan",
			"luis" };

	private static final int QUESTIONS = 4;

	public static void main(String args[]) throws InterruptedException {
		Phaser startQuestion = new Phaser() {
			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase >= QUESTIONS;
			}
		};

		Phaser finishExam = new Phaser();

		ExecutorService executor = Executors.newCachedThreadPool();
		executor.execute(new Teacher3(finishExam));
		finishExam.register();
		for (int i = 0; i < STUDENTS.length; ++i) {
			// create and start threads
			startQuestion.register();
			finishExam.register();
			executor.execute(new Student3(startQuestion, finishExam,
					STUDENTS[i]));
		}

		executor.shutdown();
		executor.awaitTermination(1, TimeUnit.DAYS);
		System.out.println("End ");
	}

}

class Student3 implements Runnable {

	private final Phaser startQuestion;
	private final Phaser finishExam;
	private final String name;

	public Student3(Phaser startQuestion, Phaser finishExam, String name) {
		this.startQuestion = startQuestion;
		this.finishExam = finishExam;
		this.name = name;
	}

	@Override
	public void run() {
		System.out.println(name + " is ready to start the exam");
	
		do {
			int question = startQuestion.arriveAndAwaitAdvance();
			if (startQuestion.isTerminated()) {
				break;
			}
			doWork(question);
		} while (true);

		// doesn't wait for others to arrive
		finishExam.arrive();
	}

	void doWork(int question) {
		try {
			// Do something
			Thread.sleep(10);
			System.out.println(name + " has finish question " + question);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}
}

class Teacher3 implements Runnable {

	private final Phaser finishExam;

	public Teacher3(Phaser finishExam) {
		this.finishExam = finishExam;
	}

	@Override
	public void run() {
		finishExam.arriveAndAwaitAdvance();
		doWork();
	}

	void doWork() {
		System.out.println("Teacher can leave");
	}
}
