package com.examples.syncblock;

public class SyncBlock2Locks {

	private static final int ITERATIONS = 50;

	private int valueA;
	private int valueB;

	private Object valueALock = new Object();
	private Object valueBLock = new Object();

	public void incrementA() {
		synchronized (valueALock) {
			valueA++;
		}
	}

	public void incrementB() {
		synchronized (valueBLock) {
			valueB++;
		}
	}

	public void decrementA() {
		synchronized (valueALock) {
			valueA--;
		}
	}

	public void decrementB() {
		synchronized (valueBLock) {
			valueB--;
		}
	}
	
	public int getValueA(){
		return this.valueA;
	}
	
	public int getValueB(){
		return this.valueB;
	}

	public static void main(String args[]) throws InterruptedException {
		final SyncBlock2Locks counter = new SyncBlock2Locks();
		Runnable incrementer = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					counter.incrementA();
					counter.incrementB();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Runnable decrementer = new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < ITERATIONS; i++) {
					counter.decrementA();
					counter.decrementB();
					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		};

		Thread t1 = new Thread(incrementer);
		Thread t2 = new Thread(decrementer);
		t1.start();
		t2.start();
		t1.join();
		t2.join();
		System.out.println(counter.getValueA() + counter.getValueB());
	}
}
