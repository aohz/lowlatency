package demo;

import demo.disruptor.LMAXSetup;

public class App {
	/* ---[ Main ]--- */
	public static void main(String[] args) {
		new LMAXSetup().init().engage();
	}

}
