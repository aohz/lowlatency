package solution.solution3;

import practice.Account;
import solution.AccountTest;

public class AccountConcurrentLinkedQueueTest extends AccountTest {

	@Override
	protected Account getInstance() {
		return new AccountConcurrentLinkedQueue();
	}

}
