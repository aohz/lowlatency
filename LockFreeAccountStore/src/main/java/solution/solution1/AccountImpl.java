package solution.solution1;

import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import practice.Account;
import solution.solution2.Operation;

/**
 * This class was a first approach to solve the problem but it was discarted because
 * creating a new ArrayList in each "do while" iteration it was a big performance penalty
 * 
 * @author AOHZ
 * 
 */
public class AccountImpl implements Account {

	static final byte DEPOSIT_OPERATION = 0;
	static final byte WIDTHDRAW_OPERATION = 1;

	private static final String TITLE = "Date		Time	Amount	       deposited	        Amount         withdrawn \n";
	private static final String LINE = "---------------------------------------------------------------------------------------";

	private AtomicReference<AccountStore> accountStore = new AtomicReference<AccountStore>(
			new AccountStore());

	@Override
	public void deposit(long amount) {
		AccountStore prev, newValue;
		List<Operation> tmp;
		long totalAmount;
		do {
			prev = accountStore.get();
			totalAmount = prev.getTotalAmount() + amount;
			// it is thread-safe because nobody will modify the original
			// operations collections while it is being copied.
			tmp = new ArrayList<Operation>(prev.getOperations());
			tmp.add(new Operation(new Date().getTime(), totalAmount,
					DEPOSIT_OPERATION, amount));
			newValue = new AccountStore(totalAmount, tmp);

		} while (!accountStore.compareAndSet(prev, newValue));
	}

	@Override
	public boolean withdraw(long amount) {
		AccountStore prev, newValue;
		List<Operation> tmp;
		long totalAmount;
		do {
			prev = accountStore.get();
			totalAmount = prev.getTotalAmount() - amount;
			if (totalAmount < 0) {
				return false;
			}
			tmp = new ArrayList<Operation>(prev.getOperations());
			tmp.add(new Operation(new Date().getTime(), totalAmount,
					WIDTHDRAW_OPERATION, amount));

			newValue = new AccountStore(totalAmount, tmp);
		} while (!accountStore.compareAndSet(prev, newValue));
		return true;
	}

	@Override
	public long getTotalAmount() {
		return accountStore.get().getTotalAmount();
	}

	@Override
	public void report(Writer out) throws IOException {
		out.write(TITLE);
		out.write(LINE);
		out.write(accountStore.toString());
	}

	@Override
	public boolean withdrawWithAdditionalOperationType(long amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
