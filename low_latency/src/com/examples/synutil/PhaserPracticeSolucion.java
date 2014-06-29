package com.synutil;

import java.util.concurrent.Phaser;

public class PhaserPracticeSolucion {

	private static final int WORKERS = 1;

	static final int ITERATIONS = 10;

	public static void main(String[] args) {

		final Phaser phaser = new Phaser(WORKERS + 1);

		final String[] lane = new String[ITERATIONS];

		new Thread("Producer") {
						
			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					
					System.out.println(phaser.toString());
					
					$sleep(20);

					lane[phaser.getPhase()] = "lane1-answer-" + i;					

					System.out
							.printf("[%-17s] working in lane1 finished phase [%d]%n",
									Thread.currentThread().getName(),
									phaser.getPhase());

					phaser.arriveAndAwaitAdvance();
				}
			}
		}.start();

		new Thread("Slow consumer") {
			@Override
			public void run() {
				for (int start = 0; start < ITERATIONS;) {
					
					System.out.println(phaser.toString());
					
					System.out
							.printf("[%-17s] about to wait for phase [%d] completion%n",
									Thread.currentThread().getName(), start);

					int phaseInProgress = phaser.awaitAdvance(start);

					// Read all the way up to the most recent completed phases.
					for (int i = start; i < phaseInProgress; i++) {
						System.out.printf(
								"[%-17s] read [%s] from phase [%d]%n", Thread
										.currentThread().getName(), lane[i], i);
					}

					start = phaseInProgress;

					$sleep(80);
				}
			}
		}.start();

		phaser.arriveAndDeregister();
	}

	private static void $sleep(long millis) {
		try {
			Thread.sleep(millis);
		} catch (InterruptedException e) {
		}
	}

}
