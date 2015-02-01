package solution.solution1;

import practice.Account;
import solution.AccountTest;

public class AccountImplTest extends AccountTest {

	@Override
	protected Account getInstance() {
		return new AccountImpl();
	}

}
