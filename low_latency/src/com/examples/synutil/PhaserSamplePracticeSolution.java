package com.examples.synutil;

import java.util.concurrent.Phaser;

public class PhaserSamplePracticeSolution {

	public static void main(String[] args) {

		Phaser phaser = new Phaser();
		phaser.register();
		new Thread(new PhaserSampleRunnable1(phaser)).start();
		new Thread(new PhaserSampleRunnable1(phaser)).start();
		phaser.arriveAndDeregister();
	}

}

class PhaserSampleRunnable1 implements Runnable {

	private Phaser phaser;

	public PhaserSampleRunnable1(Phaser phaser) {
		//the register can bo inside run method because we must be sure that
		//both threads register before execution (one could call register and arriveAndAwaitAdvance in a row)
		phaser.register();
		this.phaser = phaser;
	}

	public void run() {
		
		System.out.println(Thread.currentThread().getName()
				+ " waiting at phase 1");
		phaser.arriveAndAwaitAdvance();
		
		System.out.println(Thread.currentThread().getName()
				+ " waiting at phase 2");
		phaser.arriveAndAwaitAdvance();

		System.out.println(Thread.currentThread().getName() + " done!");

	}
}
