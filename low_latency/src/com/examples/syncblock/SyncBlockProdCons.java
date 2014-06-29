package com.examples.syncblock;

public class SyncBlockProdCons {
	private static final int ITERATIONS = 10;
	// Message sent from producer
	// to consumer.
	private String message;
	// True if consumer should wait
	// for producer to send message,
	// false if producer should wait for
	// consumer to retrieve message.
	private boolean empty = true;

	public synchronized String take() {
		// Wait until message is available.
		while (empty) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		System.out.println("Taking " + message);
		// Toggle status.
		empty = true;
		// Notify producer that status has changed.
		notifyAll();
		return message;
	}

	public synchronized void put(String message) {
		// Wait until message has been retrieved.
		while (!empty) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		// Toggle status.
		empty = false;
		// Store message.
		this.message = message;
		System.out.println("Putting " + message);
		// Notify consumer that status has changed.
		notifyAll();
	}

	public static void main(String[] args) throws InterruptedException {
		final SyncBlockProdCons messageProdCon = new SyncBlockProdCons();

		Thread t1 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++)
					messageProdCon.put("Message " + i);

			}

		});

		Thread t2 = new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++)
					messageProdCon.take();
			}

		});

		t1.start();
		t2.start();

		t1.join();
		t2.join();
	}

}