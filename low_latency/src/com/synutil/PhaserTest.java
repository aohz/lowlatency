package com.synutil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserTest {

	private static final int TASK_NUMBER = 2;

	public static void main(String[] args) throws InterruptedException {

		ExecutorService executor = Executors.newCachedThreadPool();

		final Phaser phaser = new Phaser();

		phaser.register();

		for (int i = 0; i < TASK_NUMBER; i++) {
			phaser.register();
			executor.execute(new MyTask(phaser));
		}

		phaser.arriveAndDeregister();
		executor.shutdown();
	}
}

class MyTask implements Runnable {

	private Phaser phaser;

	public MyTask(Phaser phaser) {
		this.phaser = phaser;
	}

	@Override
	public void run() {
		do {
			System.out.println(Thread.currentThread().getName() + " Phase: "
					+ phaser.arriveAndAwaitAdvance());
			doLogic(phaser.getPhase());
		} while (phaser.getPhase() < 2);
	}

	private void doLogic(int logic) {
		try {
			Thread.sleep(50);
			System.out.println(Thread.currentThread().getName() + " Logic: "
					+ logic);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
};
