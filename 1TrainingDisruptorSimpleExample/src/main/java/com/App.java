package com;

import com.disruptor.ValueProcessor;
import com.model.Value;

public class App {

	private static final int MAX_EVENTS = 10;

	private ValueProcessor processor;

	public static void main(String[] args) {
		App app = new App();
		app.processor = new ValueProcessor();
		app.processor.init();

		Thread pA = new Thread(app.new PublisherA());
		pA.start();
	}

	class PublisherA implements Runnable {
			
		public void run() {
			for (int i = 0; i < MAX_EVENTS; i++) {
				processor.publishEvent(new Value("" + i));
				Thread.yield();
			}
		}
	}
}
