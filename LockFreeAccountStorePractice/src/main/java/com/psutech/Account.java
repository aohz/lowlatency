package com.psutech;

import java.io.IOException;
import java.io.Writer;

/**
 * At no time should the amount stored fall below zero. All methods will be
 * invoked from multiple threads. A lock-free implementation is preferred.
 */
public interface Account {

	public static final byte DEPOSIT_OPERATION = 0;
	public static final byte WIDTHDRAW_OPERATION = 1;
	public static final byte FAILED_WIDTHDRAW_OPERATION = 2;

	void deposit(long amount);

	/**
	 * @returns true if the account had sufficient amount of money to allow
	 *          withdrawal of the amount and the account was updated false
	 *          otherwise
	 */
	boolean withdraw(long amount);

	/**
	 * @returns true if the account had sufficient amount of money to allow
	 *          withdrawal of the amount and the account was updated false
	 *          otherwise
	 *          If false, the failed operation is logged.
	 * **/
	boolean withdrawWithAdditionalOperationType(long amount);

	/**
	 * Prints the following table:
	 * 
	 * Date Time Amount deposited Amount withdrawn
	 * ------------------------------------------------------------------
	 * 2013-04-04 20:30 2000 2013-04-04 20:30 500 ...
	 */
	void report(final Writer out) throws IOException;

	public long getTotalAmount();

}
