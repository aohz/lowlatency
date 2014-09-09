package com.psutech;

import java.util.Date;

public class Operation implements Comparable<Operation> {

	private static final String DEPOSIT_OPERATION_FMT = "%n%4$d-%1$td/%1$tm/%1$ty       %1$tl:%1$tm:%1$tS %2$6d %3$15d";
	private static final String WIDTHDRAW_OPERATION_FMT = "%n%4$d-%1$td/%1$tm/%1$ty       %1$tl:%1$tm:%1$tS %2$45d %3$15d";
	private static final String FAILED_OPERATION_FMT = "%n%4$d-%1$td/%1$tm/%1$ty       %1$tl:%1$tm:%1$tS %2$45d %3$15d NOT ALLOWED";

	private long order;
	private final long timestamp;
	private final long totalAmount;
	private final byte operationType;
	private final long amount;

	public Operation(long timestamp, long totalAmount, byte operationType,
			long amount) {
		this.timestamp = timestamp;
		this.totalAmount = totalAmount;
		this.amount = amount;
		this.operationType = operationType;
	}

	public Operation(long order, long timestamp, long totalAmount,
			byte operationType, long amount) {
		this.order = order;
		this.timestamp = timestamp;
		this.totalAmount = totalAmount;
		this.amount = amount;
		this.operationType = operationType;
	}

	public String toString() {
		if (operationType == Account.DEPOSIT_OPERATION) {
			return String.format(DEPOSIT_OPERATION_FMT, new Date(timestamp),
					totalAmount, amount, order);
		}
		if (operationType == Account.WIDTHDRAW_OPERATION) {
			return String.format(WIDTHDRAW_OPERATION_FMT, new Date(timestamp),
					totalAmount, amount, order);
		}
		
		return String.format(FAILED_OPERATION_FMT, new Date(timestamp),
				totalAmount, amount, order);
	}

	@Override
	public int compareTo(Operation o) {
		return this.order == o.getOrder() ? 0 : (this.order < o.getOrder() ? -1
				: 1);
	}

	public long getOrder() {
		return order;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

}
