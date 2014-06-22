package com.disruptor;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import com.disruptor.event.ValueEvent;
import com.disruptor.handler.GenericExceptionHandler;
import com.disruptor.handler.PostHandler;
import com.disruptor.handler.ValueHandler;
import com.disruptor.translator.ValueEventTranslator;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.model.Value;

public class ValueProcessor {

	private Disruptor<ValueEvent> disruptor;
	private Executor executor = Executors.newFixedThreadPool(2);
	private static final int BUFFER_SIZE = 1024;

	@SuppressWarnings("unchecked")
	public void init() {

		disruptor = new Disruptor<ValueEvent>(ValueEvent.VALUE_EVENT_FACTORY,
				BUFFER_SIZE, executor, ProducerType.SINGLE,
				new BlockingWaitStrategy());

		EventHandler<ValueEvent> handler1 = new ValueHandler("h1");
		EventHandler<ValueEvent> postHandler = new PostHandler("P1");

		disruptor.handleEventsWith(handler1).then(postHandler);
		
		ExceptionHandler exceptionHandler = new GenericExceptionHandler();
		disruptor.handleExceptionsFor(handler1).with(exceptionHandler);
		disruptor.handleExceptionsFor(postHandler).with(exceptionHandler);

		disruptor.start();
		
		
	}

	public void publishEvent(Value value) {		
		disruptor.publishEvent(new ValueEventTranslator(value));
	}
}
