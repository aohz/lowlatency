package com.distruptor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.distruptor.event.TransactionEvent;

/**
 * This handler uses the event to update the in-memory data store. Operations on
 * this store always happen in a single thread, so concurrency issues are non-
 * existent for these updates.
 */
public class PostTransactionHandler extends AbstractEventHandler {
	private static final Logger logger = LoggerFactory
			.getLogger(PostTransactionHandler.class);

	@Override
	public void onEvent(TransactionEvent event, long sequence,	boolean endOfBatch) throws Exception {
		
		logger.debug("POSTED VALID TRANSACTION -> {}", event.getTransaction().toString());
	}
}