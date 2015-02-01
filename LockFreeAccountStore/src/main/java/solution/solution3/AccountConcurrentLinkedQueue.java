package solution.solution3;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicReference;

import practice.Account;

import solution.solution2.Operation;

public class AccountConcurrentLinkedQueue implements Account {

	private static final String TITLE = "Date		Time	Amount	       deposited	        Amount         withdrawn \n";
	private static final String LINE = "---------------------------------------------------------------------------------------";

	private AtomicReference<Operation> atomicTotal = new AtomicReference<>(
			new Operation(0, 0, 0, DEPOSIT_OPERATION, 0));
	private ConcurrentLinkedQueue<Operation> history = new ConcurrentLinkedQueue<>();

	@Override
	public void deposit(long amount) {
		Operation prev, newValue;
		long newTotalAmount, newOrder;
		do {
			prev = atomicTotal.get();
			newTotalAmount = prev.getTotalAmount() + amount;
			newOrder = prev.getOrder() + 1;
			newValue = new Operation(newOrder, new Date().getTime(),
					newTotalAmount, DEPOSIT_OPERATION, amount);
		} while (!atomicTotal.compareAndSet(prev, newValue));
		history.offer(newValue);
	}

	@Override
	public boolean withdraw(long amount) {
		Operation prev, newValue;
		long newTotalAmount, newOrder;
		do {
			prev = atomicTotal.get();
			newTotalAmount = prev.getTotalAmount() - amount;
			newOrder = prev.getOrder() + 1;
			newValue = new Operation(newOrder, new Date().getTime(), newTotalAmount, WIDTHDRAW_OPERATION, amount);
			// ///////////////////////////////////////////////////////////////////////////////
			// check that the value has not been changed since the last time we
			// read it
			// ///////////////////////////////////////////////////////////////////////////////
			if (!atomicTotal.compareAndSet(prev, prev)) {
				// if the value has been changed, the operation retried
				continue;
			}
			if (newTotalAmount < 0) {
				// return not to log the operation if it can't be performed
				return false;
			}
			if (atomicTotal.compareAndSet(prev, newValue)) {
				// if the value has not been change, the new value is set
				break;
			}
		} while (true);
		history.offer(newValue);
		return true;
	}

	/**
	 * Another implementation of the withdraw method. In this implementation,
	 * all operations are logged even when there aren't enough money to complete
	 * the operation
	 */
	public boolean withdrawWithAdditionalOperationType(long amount) {
		Operation prev, newValue;
		do {
			prev = atomicTotal.get();
			boolean error = prev.getTotalAmount() > amount;
			newValue = new Operation(error ? prev.getOrder()
					: prev.getOrder() + 1, new Date().getTime(),
					error ? prev.getTotalAmount() - amount : prev
							.getTotalAmount(), error ? WIDTHDRAW_OPERATION
							: FAILED_WIDTHDRAW_OPERATION, amount);
			if (atomicTotal.compareAndSet(prev, newValue)) {
				break;
			}
		} while (true);
		history.offer(newValue);
		return true;
	}

	@Override
	public void report(Writer out) throws IOException {
		SortedSet<Operation> sortedHistory = new TreeSet<>(this.history);
		out.write(TITLE);
		out.write(LINE);
		for (Operation operation : sortedHistory) {
			out.write(operation.toString());
		}
	}

	@Override
	public long getTotalAmount() {
		return atomicTotal.get().getTotalAmount();
	}

}
