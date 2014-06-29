package com.examples.synutil;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

public class CiclicBarrierSimple {

	private static final int N = 10;

	public static void main(String[] args) {
		Runnable barrier1Action = new Runnable() {
			public void run() {
				System.out.println("BarrierAction 1 executed ");
			}
		};

		CyclicBarrier barrier = new CyclicBarrier(N, barrier1Action);
		for (int i = 0; i < N; i++) {
			new Thread(new cbRunnable(barrier)).start();
		}
	}

}

class cbRunnable implements Runnable {

	CyclicBarrier barrier = null;

	public cbRunnable(CyclicBarrier barrier1) {

		this.barrier = barrier1;

	}

	public void run() {
		try {
			// await(); await(ms),
			// getNumberWaiting(), getParties(),
			// isBroken(), reset()
			// do something and wait for another thread
			System.out.println("Arrival order: " + 
			this.barrier.await());
			// do something and wait for another thread
			this.barrier.await();
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (BrokenBarrierException e) {
			e.printStackTrace();
		}
	}
}
