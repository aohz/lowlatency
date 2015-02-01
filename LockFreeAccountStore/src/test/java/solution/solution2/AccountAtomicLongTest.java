package solution.solution2;

import practice.Account;
import solution.AccountTest;
import solution.solution1.AccountImpl;

public class AccountAtomicLongTest extends AccountTest {

	@Override
	protected Account getInstance() {
		return new AccountImpl();
	}

}
