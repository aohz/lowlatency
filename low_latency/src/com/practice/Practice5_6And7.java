package com.practice;

public class Practice5_6And7 {

	private static final String[] STUDENTS = new String[] { "ana", "juan",
			"luis" };

	public static void main(String args[]) throws InterruptedException {

		System.out.println("End ");
	}

}

class Student implements Runnable {

	private final String name;

	Student(String name) {
		this.name = name;
	}

	@Override
	public void run() {
		doWork();
	}

	void doWork() {
		// Do something
		System.out.println(name + " has finish");
	}
}

class Teacher implements Runnable {

	@Override
	public void run() {

		doWork();

	}

	void doWork() {
		System.out.println("Teacher can leave");
	}
}
