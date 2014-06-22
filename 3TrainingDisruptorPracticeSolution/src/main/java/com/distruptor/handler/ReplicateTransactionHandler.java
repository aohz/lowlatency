package com.distruptor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.distruptor.event.TransactionEvent;

/**
 * In the real world, this handler would replicate the transaction event to in-memory 
 * date stores running on one or more other nodes as part of a redundancy strategy.
 */
public class ReplicateTransactionHandler extends AbstractEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(ReplicateTransactionHandler.class);

    @Override
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws Exception {
        logger.warn("REPLICATE -> {}", event.getTransaction().toString());
    }
}
