package com.synutil;

import java.util.concurrent.Semaphore;

public class SemaphorePool {

	private static final int ITERATIONS = 10;

	private static final int MAX_AVAILABLE = 2;
	private final Semaphore available = new Semaphore(MAX_AVAILABLE, true);

	public Printer getItem() throws InterruptedException {
		// available.acquire(permits), available.release(permits),
		// available.tryAcquire(permits), available.availablePermits()
		available.acquire();

		return getNextAvailableItem();

	}

	public void putItem(Printer x) {
		if (markAsUnused(x))
			available.release();
	}

	// Not a particularly efficient data structure; just for demo

	protected Printer[] items = { new Printer(1), new Printer(2),
			new Printer(3) };
	protected boolean[] used = new boolean[MAX_AVAILABLE];

	protected synchronized Printer getNextAvailableItem() {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (!used[i]) {
				used[i] = true;
				return items[i];
			}
		}
		return null; // not reached
	}

	protected synchronized boolean markAsUnused(Printer item) {
		for (int i = 0; i < MAX_AVAILABLE; ++i) {
			if (item == items[i]) {
				if (used[i]) {
					used[i] = false;
					return true;
				} else
					return false;
			}
		}
		return false;
	}

	public static void main(String[] args) throws InterruptedException {
		final SemaphorePool pool = new SemaphorePool();

		Runnable usingPrinter = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					Printer p;
					try {
						p = pool.getItem();
						System.out.println(Thread.currentThread().getName()
								+ " Using" + p.id);
						pool.putItem(p);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}

				}

			}

		};

		Thread t1 = new Thread(usingPrinter);
		Thread t2 = new Thread(usingPrinter);
		Thread t3 = new Thread(usingPrinter);

		t1.start();
		t2.start();
		t3.start();

		t1.join();
		t2.join();
		t2.join();
	}

	class Printer {
		int id;

		public Printer(int id) {
			this.id = id;
		}
	}
}
