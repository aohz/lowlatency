package com.disruptor.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.disruptor.event.ValueEvent;
import com.lmax.disruptor.EventHandler;

public class PostHandler implements EventHandler<ValueEvent> {

	private static final Logger logger = LoggerFactory
			.getLogger(PostHandler.class);

	private String name;

	public PostHandler(String name) {
		this.name = name;
	}

	public void onEvent(ValueEvent event, long sequence, boolean endOfBatch)
			throws Exception {
		logger.debug(this.name + "_" + event.getValue().toString());

	}
}
