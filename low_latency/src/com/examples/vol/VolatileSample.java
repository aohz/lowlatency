package com.examples.vol;

public class VolatileSample {

	private static boolean ready;
	private static int number;

	static int foo;

	private static class ReaderThread extends Thread {
		public void run() {
			while (!ready) {
				foo++;
				//Thread.yield();
			}
			System.out.println(number);
		}
	}

	public static void main(String[] args) throws Exception {
		new ReaderThread().start();
		number = 42;
		Thread.sleep(1000);
		ready = true;
		System.out.println("just to prove foo is updated as you go along:"
				+ foo);
	}
}
