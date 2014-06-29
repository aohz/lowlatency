package com.examples.synutil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserTest2 {

	private static final int TASK_NUMBER = 2;

	public static void main(String[] args) throws InterruptedException {

		ExecutorService executor = Executors.newCachedThreadPool();

		final Phaser phaser = new Phaser(1) {
			protected boolean onAdvance(int phase, int registeredParties) {
				return phase >= 1 || registeredParties == 0;
			}
		};

		for (int i = 0; i < TASK_NUMBER; i++) {
			phaser.register();
			executor.execute(new MyTask2(phaser));
		}

		phaser.arriveAndDeregister();
		executor.shutdown();
	}
}

class MyTask2 implements Runnable {

	private Phaser phaser;

	public MyTask2(Phaser phaser) {
		this.phaser = phaser;
	}

	@Override
	public void run() {
		do {
			System.out.println(Thread.currentThread().getName() + " Phase: "
					+ phaser.arriveAndAwaitAdvance());
			doLogic(phaser.getPhase());
		} while (!phaser.isTerminated());
	}

	private void doLogic(int logic) {
		try {
			Thread.sleep(50);
			System.out.println(Thread.currentThread().getName() + " Logic: " + logic);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
};
