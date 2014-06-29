package com.atomic;

import java.util.concurrent.atomic.AtomicInteger;

public class AtomicCounter {

	private final AtomicInteger value = new AtomicInteger(0);

	public int getValue() {
		return value.get();
	}

	public int getNextValue() {
		return value.incrementAndGet();
	}

	public int getPreviousValue() {
		return value.decrementAndGet();
	}

	public void setValue(int value) {
		this.value.set(value);
	}
}
