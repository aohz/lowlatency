package solution;

import java.io.IOException;
import java.io.PrintWriter;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import practice.Account;

import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;

@RunWith(JUnit4.class)
public abstract class AccountTest {

	private static Account accountDeposit;
	private static Account accountWithdraw;
	private static Account accountNoNegative;
	private static Account accountDepositWithdraw;
	private static Account accountReport;

	protected abstract Account getInstance();

	@Rule
	public ConcurrentRule rule = new ConcurrentRule();

	@Before
	public void setup() {
		if (accountDeposit == null) {
			accountDeposit = getInstance();
			accountWithdraw = getInstance();
			accountNoNegative = getInstance();
			accountDepositWithdraw = getInstance();
			accountReport = getInstance();

			accountWithdraw.deposit(100);
			accountNoNegative.deposit(100);
			accountDepositWithdraw.deposit(100);
		}
	}

	@Test
	@Concurrent(count = 5)
	public void test_deposit() {
		accountDeposit.deposit(10);
	}

	@Test
	@Concurrent(count = 5)
	public void test_withdraw() {
		accountWithdraw.withdraw(10);
	}

	@Test
	@Concurrent(count = 5)
	public void test_accountNoNegative() {
		accountNoNegative.withdraw(20);
	}

	@Test
	@Concurrent(count = 5)
	public void test_accountDepositWithdraw() {
		accountDepositWithdraw.withdraw(1);
		Thread.yield();
		accountDepositWithdraw.deposit(1);
	}

	@Test
	@Concurrent(count = 5)
	public void testReport() throws IOException {
		accountReport.deposit(1000);
		Thread.yield();
		accountReport.withdraw(5000);
		Thread.yield();
		accountReport.deposit(1000);
		Thread.yield();
		accountReport.withdraw(500);
		Thread.yield();
		try (PrintWriter writer = new PrintWriter(System.out)) {
			accountReport.report(writer);
			writer.flush();
		}
	}

	@Test
	@Concurrent(count = 5)
	public void test_Report_loggingOperationWhenFail() throws IOException {
		accountReport.deposit(1000);
		Thread.yield();
		accountReport.withdrawWithAdditionalOperationType(5000);
		Thread.yield();
		accountReport.deposit(1000);
		Thread.yield();
		accountReport.withdrawWithAdditionalOperationType(500);
		Thread.yield();
		try (PrintWriter writer = new PrintWriter(System.out)) {
			accountReport.report(writer);
			writer.flush();
		}
	}

	@AfterClass
	public static void assertions() throws IOException {
		assertTestDeposit();
		assertTestWithdraw();
		assertTestAccountNoNegative();
		assertAccountDepositWithdraw();
	}

	private static void assertTestWithdraw() {
		Assert.assertEquals("test_withdraw", 50,
				accountWithdraw.getTotalAmount());
	}

	private static void assertTestDeposit() {
		Assert.assertEquals("test_deposit", 50, accountDeposit.getTotalAmount());
	}

	private static void assertTestAccountNoNegative() {
		Assert.assertTrue("test_noNegativeValues",
				accountNoNegative.getTotalAmount() >= 0);
	}

	private static void assertAccountDepositWithdraw() {
		Assert.assertEquals("test_accountDepositWithdraws", 100,
				accountDepositWithdraw.getTotalAmount());
	}
}
