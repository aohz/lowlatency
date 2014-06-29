package com.examples.synutil;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Phaser;

public class PhaserPracticeSolucion2 {

	private static final int WORKERS = 1;

	static final int ITERATIONS = 1000;

	public static void main(String[] args) {

		Phaser phaser = new Phaser(WORKERS + 1);

		String[] lane = new String[ITERATIONS];

		ExecutorService executor = Executors.newFixedThreadPool(2);
		executor.execute(new PhaProducer(phaser, lane));
		executor.execute(new PhaConsumer(phaser, lane));

		phaser.arriveAndDeregister();
		
		executor.shutdown();
	}

}

class PhaProducer implements Runnable {

	private Phaser phaser;
	private String[] lane;

	public PhaProducer(Phaser phaser, String[] lane) {
		super();		
		this.phaser = phaser;
		this.lane = lane;
	}

	@Override
	public void run() {		
		try {
			for (int i = 0; i < PhaserPracticeSolucion2.ITERATIONS; i++) {				
				Thread.sleep(10);

				lane[phaser.getPhase()] = "lane1-answer-" + i;

				System.out.printf(
						"[%-17s] working in lane1 finished phase [%d]%n",
						Thread.currentThread().getName(), phaser.getPhase());

				phaser.arriveAndAwaitAdvance();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}

class PhaConsumer implements Runnable {

	private Phaser phaser;
	private String[] lane;

	public PhaConsumer(Phaser phaser, String[] lane) {
		super();
		this.phaser = phaser;
		this.lane = lane;
	}

	@Override
	public void run() {
		for (int start = 0; start < PhaserPracticeSolucion2.ITERATIONS;) {

			System.out.printf(
					"[%-17s] about to wait for phase [%d] completion%n", Thread
							.currentThread().getName(), start);

			int phaseInProgress = phaser.awaitAdvance(start);

			// Read all the way up to the most recent completed phases.
			for (int i = start; i < phaseInProgress; i++) {
				System.out.printf("[%-17s] read [%s] from phase [%d]%n", Thread
						.currentThread().getName(), lane[i], i);
			}

			start = phaseInProgress;

			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
