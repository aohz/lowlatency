package com.distruptor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.distruptor.event.TransactionEvent;

/**
 * In the real world, this handler would write the transaction event to a durable journal, such
 * that these events can be re-played after a system failure, to rebuild the state of the
 * in memory store. In this example handler, we just do a simplistic file write as a stand-in
 * for a more sophisticated approach.
 */
public class JournalTransactionHandler extends AbstractEventHandler {
    private static final Logger logger = LoggerFactory.getLogger(JournalTransactionHandler.class);
    
    @Override
    public void onEvent(TransactionEvent event, long sequence, boolean endOfBatch) throws Exception {
    
        logger.debug("JOURNALED TRANSACTION -> {}", event.getTransaction().toString());
    }
}
