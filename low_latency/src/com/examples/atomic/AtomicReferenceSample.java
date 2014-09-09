package com.examples.atomic;

import java.util.concurrent.atomic.AtomicReference;

public class AtomicReferenceSample {
	
	private AtomicReference<AccessStatistics> stats = new AtomicReference<AccessStatistics>(new AccessStatistics(0, 0));

	public void incrementPageCount(boolean wasError) {
		AccessStatistics prev, newValue;
		do {
			prev = stats.get();
			int noPages = prev.getNoPages() + 1;
			int noErrors = prev.getNoErrors();
			if (wasError) {
				noErrors++;
			}
			newValue = new AccessStatistics(noPages, noErrors);
		} while (!stats.compareAndSet(prev, newValue));
	}
}

class AccessStatistics {
	private final int noPages, noErrors;

	public AccessStatistics(int noPages, int noErrors) {
		this.noPages = noPages;
		this.noErrors = noErrors;
	}

	public int getNoPages() {
		return noPages;
	}

	public int getNoErrors() {
		return noErrors;
	}
}
