package com.distruptor.event;

import com.lmax.disruptor.EventFactory;
import com.model.Transaction;

/**
 * The TransactionEvent is at the core of the pattern - this is the data
 * structure with which the ring-buffer works, and represents the data for our
 * event-sourcing implementation.
 */
public class TransactionEvent {
	private Transaction transaction;
	private long bufferSeq;

	public TransactionEvent() {
	}

	public TransactionEvent(Transaction transaction, long bufferSeq) {
		this.transaction = transaction;
		this.bufferSeq = bufferSeq;
	}

	public Transaction getTransaction() {
		return transaction;
	}

	public void setTransaction(Transaction transaction) {
		this.transaction = transaction;
	}

	public long getBufferSeq() {
		return bufferSeq;
	}

	public void setBufferSeq(long bufferSeq) {
		this.bufferSeq = bufferSeq;
	}

	/**
	 * Would this go here in the real world? Maybe, maybe not.
	 */
	public String asJournalEntry() {
		return String.format("%s|%s|%s|%s|%s\n", this.getBufferSeq(), this
				.getTransaction().getDate().getTime(), this.getTransaction()
				.getAccountnbr(), this.getTransaction().getType(), this
				.getTransaction().getAmount());
	}

	/**
	 * EventFactory is specified by the disruptor framework. This is how the
	 * ring-buffer populates itself. See init() in TransactionProcessor.
	 */
	public final static EventFactory<TransactionEvent> EVENT_FACTORY = new EventFactory<TransactionEvent>() {
		public TransactionEvent newInstance() {
			return new TransactionEvent();
		}
	};
}
