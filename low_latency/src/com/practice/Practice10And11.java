package com.practice;

public class Practice10And11 {

	public static void main(String[] args) throws InterruptedException {

		long start = System.nanoTime();
		int[] values = new int[100];

		for (int i = 0; i < values.length; i++) {
			Thread.sleep(1);
			values[i] = 1;
		}

		int result = 0;
		for (int i = 0; i < values.length; i++) {
			Thread.sleep(1);
			result += values[i];
		}

		System.out.println(result);
		long end = System.nanoTime();
		System.out.println("Time(ns) :" + (end - start));
	}
}

