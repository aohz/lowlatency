package com.psutech;

import java.util.ArrayList;
import java.util.List;

public class AccountStore {

	private final long totalAmount;
	private final List<Operation> accountHistory;

	public AccountStore() {
		this.totalAmount = 0;
		accountHistory = new ArrayList<Operation>();
	}
	
	public AccountStore(long totalAmount, List<Operation> accountHistory) {
		this.totalAmount = totalAmount;
		this.accountHistory = accountHistory;
	}
	

	public long getTotalAmount() {
		return totalAmount;
	}

	public List<Operation> getOperations() {
		return this.accountHistory;
	}	

	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (Operation accountHistory : this.accountHistory) {
			sb.append(accountHistory.toString());
		}
		return sb.toString();
	}

}
