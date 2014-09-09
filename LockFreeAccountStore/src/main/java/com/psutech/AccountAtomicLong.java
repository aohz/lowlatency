package com.psutech;

import java.io.IOException;
import java.io.Writer;
import java.util.Date;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicLong;

public class AccountAtomicLong implements Account {
	
	private static final String TITLE = "Date		Time	Amount	       deposited	        Amount         withdrawn \n";
	private static final String LINE = "---------------------------------------------------------------------------------------";

	private AtomicLong atomicTotal = new AtomicLong(0);
	private ConcurrentLinkedQueue<Operation> history = new ConcurrentLinkedQueue<>();

	@Override
	public void deposit(long amount) {		
		long prev, newValue;		
		Operation operation;		
		do {
			prev = atomicTotal.get();
			newValue = prev + amount;					
			operation = new Operation(System.nanoTime(), new Date().getTime(), newValue, DEPOSIT_OPERATION, amount);
		} while (!atomicTotal.compareAndSet(prev, newValue));
		history.offer(operation);
	}

	@Override
	public boolean withdraw(long amount) {
		long prev, newValue;		
		Operation operation;
		do {
			prev = atomicTotal.get();
			newValue = prev - amount;	
			if (newValue < 0) {
				return false;
			}
			operation = new Operation(System.nanoTime(), new Date().getTime(), newValue, WIDTHDRAW_OPERATION, amount);
		} while (!atomicTotal.compareAndSet(prev, newValue));	
		history.offer(operation);
		return true;
	}

	@Override
	public void report(Writer out) throws IOException {
		SortedSet<Operation> sortedHistory = new TreeSet<>(this.history);		
		out.write(TITLE);
		out.write(LINE);
		for(Operation operation: sortedHistory){
			out.write(operation.toString());	
		}		
	}
	
	@Override
	public long getTotalAmount() {
		return atomicTotal.get();
	}

	@Override
	public boolean withdrawWithAdditionalOperationType(long amount) {
		// TODO Auto-generated method stub
		return false;
	}

	
}	

