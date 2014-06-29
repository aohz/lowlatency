package com.examples.synutil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Exchanger;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ExchangerPracticeSolucion {

	public static final int ITERATIONS = 10;

	public static void main(String[] args) {

		ExecutorService executor = Executors.newFixedThreadPool(2);

		Exchanger<List<String>> exchanger = new Exchanger<List<String>>();
		executor.execute(new ExcConsumer(exchanger));
		executor.execute(new ExcProducer(exchanger));

		executor.shutdown();
	}

}

class ExcConsumer implements Runnable {

	private Exchanger<List<String>> exchanger;
	private List<String> values = new ArrayList<String>();

	public ExcConsumer(Exchanger<List<String>> exchanger) {
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < ExchangerPracticeSolucion.ITERATIONS; i++) {
				this.values = exchanger.exchange(this.values);
				this.consume();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void consume() {
		if (this.values.isEmpty()) {
			throw new RuntimeException("List is emty");
		}

		System.err.println(new Date() + " Consume " + values);
		values.clear();
	}

}

class ExcProducer implements Runnable {

	private Exchanger<List<String>> exchanger;
	private List<String> values = new ArrayList<String>();

	public ExcProducer(Exchanger<List<String>> exchanger) {
		this.exchanger = exchanger;
	}

	@Override
	public void run() {
		try {
			for (int i = 0; i < ExchangerPracticeSolucion.ITERATIONS; i++) {
				produce();
				this.values = exchanger.exchange(this.values);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private void produce() {
		if (!this.values.isEmpty()) {
			throw new RuntimeException("List is not emty");
		}
		for (int i = 0; i < 10; i++) {
			this.values.add("A");
		}
		try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {			
			e.printStackTrace();
		}
	}

}
