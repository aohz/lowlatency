package com.distruptor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.distruptor.event.TransactionEvent;
import com.lmax.disruptor.WorkHandler;

public class DemoWorkHandler implements WorkHandler<TransactionEvent> {
	
	private static final Logger logger = LoggerFactory.getLogger(DemoWorkHandler.class);
	
	public void onEvent(TransactionEvent event) throws Exception {
		logger.debug(event.getTransaction().toString());
	}
}
