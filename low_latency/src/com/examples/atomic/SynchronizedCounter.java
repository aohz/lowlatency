package com.examples.atomic;

public class SynchronizedCounter {

	private int value;

	public synchronized int getValue() {
		return value;
	}

	public synchronized int getNextValue() {
		return ++value;
	}

	public synchronized int getPreviousValue() {
		return --value;
	}

	public synchronized void setValue(int value) {
		this.value = value;
	}

}
