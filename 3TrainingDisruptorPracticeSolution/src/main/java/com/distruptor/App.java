package com.distruptor;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.model.Transaction;

public class App {

	public static void main(String[] args) throws Exception {
		TransactionProcessor processor = new TransactionProcessor();
		processor.init();

		Thread t1 = new Thread(new DemoWorker(TEST_SET_A, processor));
		t1.setName("TEST_SET_A");
		Thread t2 = new Thread(new DemoWorker(TEST_SET_B, processor));
		t2.setName("TEST_SET_B");
		t1.start();
		t2.start();

		// Wait for the transactions to filter through, of course you would
		// usually have the transaction processor lifecycle managed by a
		// container or in some other more sophisticated way...
		t1.join();
		t2.join();

		processor.destroy();
	}

	private static final List<Transaction> TEST_SET_A = new ArrayList<Transaction>();
	static {
		TEST_SET_A.add(new Transaction("1A", new Date(), new BigDecimal(
				"1200.00"), "54321", "New Account Opened"));
		TEST_SET_A.add(new Transaction("2A", new Date(), new BigDecimal(
				"-134.56"), "54321", "Check Cleared"));
		TEST_SET_A.add(new Transaction("3A", new Date(), new BigDecimal(
				"129.78"), "12345", "Direct Deposit"));
		TEST_SET_A.add(new Transaction("4A", new Date(), new BigDecimal(
				"100.00"), "54321", "Deposit"));
		TEST_SET_A.add(new Transaction("5A", new Date(), new BigDecimal("-60"),
				"12345", "ATM Withdrawal"));
		TEST_SET_A.add(new Transaction("6A", new Date(), new BigDecimal(
				"125.56"), "12345", "Direct Deposit"));
		TEST_SET_A.add(new Transaction("7A", new Date(), new BigDecimal(
				"-568.90"), "54321", "Loan Payment"));
	}

	private static final List<Transaction> TEST_SET_B = new ArrayList<Transaction>();
	static {
		TEST_SET_B.add(new Transaction("1B", new Date(), new BigDecimal(
				"2478.45"), "66611", "Direct Deposit"));
		TEST_SET_B.add(new Transaction("2B", new Date(), new BigDecimal(
				"-234.60"), "66611", "Check Cleared"));
		TEST_SET_B.add(new Transaction("3B", new Date(), new BigDecimal(
				"400.34"), "12345", "Deposit"));
		TEST_SET_B.add(new Transaction("4B", new Date(),
				new BigDecimal("95.50"), "54321", "Deposit"));
		TEST_SET_B.add(new Transaction("5B", new Date(), new BigDecimal("-50"),
				"66611", "ATM Withdrawal"));
		TEST_SET_B.add(new Transaction("6B", new Date(), new BigDecimal(
				"-10.00"), "12345", "Service Fee"));
		TEST_SET_B.add(new Transaction("7B", new Date(), new BigDecimal(
				"-10.00"), "54321", "Service Fee"));
	}
}

class DemoWorker implements Runnable {
	private TransactionProcessor tp;
	private List<Transaction> testSet;

	public DemoWorker(List<Transaction> testSet, TransactionProcessor tp) {
		this.tp = tp;
		this.testSet = testSet;
	}

	public void run() {
		for (Transaction tx : testSet) {
			tp.postTransaction(tx);
		}
	}

}
