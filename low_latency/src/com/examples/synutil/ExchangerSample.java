package com.examples.synutil;

import java.util.concurrent.Exchanger;

public class ExchangerSample {

	public static void main(String[] args) {
		Exchanger<String> exchanger = new Exchanger<String>();

		ExchangerRunnable er1 = new ExchangerRunnable(exchanger,"A");
		ExchangerRunnable er2 = new ExchangerRunnable(exchanger,"B");

		new Thread(er1).start();
		new Thread(er2).start();
	}

}

class ExchangerRunnable implements Runnable {

	Exchanger<String> exchanger = null;
	String value = null;

	public ExchangerRunnable(Exchanger<String> exchanger, String object) {
		this.exchanger = exchanger;
		this.value = object;
	}

	public void run() {
		try {
			String previous = this.value;
			this.value = this.exchanger.exchange(this.value);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}