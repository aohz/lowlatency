package com.collections;

//2 seconds
//Do some real work here with postponed.getWorkItem()
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.DelayQueue;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;
//2 seconds
//Do some real work here with postponed.getWorkItem()

public class DelayQueueExample {

	private static final int ELEMENTS_NUMBER = 10;

	public static void main(String[] args) {

		BlockingQueue<PostponedWorkItem> queue = new DelayQueue<>();

		for (int i = 0; i < ELEMENTS_NUMBER; i++) {
			queue.add(new PostponedWorkItem(new WorkItem(), 1000));
		}

		// Block thread until an element is avalable
		for (int i = 0; i < ELEMENTS_NUMBER; i++) {
			PostponedWorkItem item;
			try {				
				item = queue.take();
				System.out.println(item.getWorkItem());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}
}

class PostponedWorkItem implements Delayed {
	private final long origin;
	private final long delay;
	private final WorkItem workItem;

	public PostponedWorkItem(final WorkItem workItem, final long delay) {
		this.origin = System.currentTimeMillis();
		this.workItem = workItem;
		this.delay = delay;
	}

	public WorkItem getWorkItem() {
		return workItem;
	}

	@Override
	public long getDelay(TimeUnit unit) {
		return unit.convert(delay - (System.currentTimeMillis() - origin),
				TimeUnit.MILLISECONDS);
	}

	@Override
	public int compareTo(Delayed delayed) {
		if (delayed == this) {
			return 0;
		}

		if (delayed instanceof PostponedWorkItem) {
			long diff = delay - ((PostponedWorkItem) delayed).delay;
			return ((diff == 0) ? 0 : ((diff < 0) ? -1 : 1));
		}

		long d = (getDelay(TimeUnit.MILLISECONDS) - delayed
				.getDelay(TimeUnit.MILLISECONDS));
		return ((d == 0) ? 0 : ((d < 0) ? -1 : 1));
	}

	@Override
	public int hashCode() {
		final int prime = 31;

		int result = 1;
		result = prime * result
				+ ((workItem == null) ? 0 : workItem.hashCode());

		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}

		if (obj == null) {
			return false;
		}

		if (!(obj instanceof PostponedWorkItem)) {
			return false;
		}

		final PostponedWorkItem other = (PostponedWorkItem) obj;
		if (workItem == null) {
			if (other.workItem != null) {
				return false;
			}
		} else if (!workItem.equals(other.workItem)) {
			return false;
		}

		return true;
	}
}

class WorkItem {
	// Some properties and methods here
	@Override
	public String toString() {
		return new Date() + "";
	}
}