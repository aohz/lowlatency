package com;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.tempusfugit.concurrency.ConcurrentRule;
import com.google.code.tempusfugit.concurrency.ConcurrentTestRunner;
import com.google.code.tempusfugit.concurrency.RepeatingRule;
import com.google.code.tempusfugit.concurrency.annotations.Concurrent;
import com.google.code.tempusfugit.concurrency.annotations.Repeating;

/**
 * 
 * 5 threads run the first method; 5 threads threads run the second method; Each
 * method is repeated 10 times. So each method is run 50 times
 * 
 * 
 * @author hernalb
 * 
 */
@RunWith(ConcurrentTestRunner.class)
public class RunConcurrentlyTest {

	@Rule
	public ConcurrentRule concurrentRule = new ConcurrentRule();
	@Rule
	public RepeatingRule repetingRule = new RepeatingRule();

	private static final AtomicInteger counter = new AtomicInteger();

	@Test
	@Repeating(repetition = 10)
	@Concurrent(count = 5)
	public void incrementCounterTest() {
		counter.getAndIncrement();
		System.out.println("A " + Thread.currentThread().getName());

	}

	@Test
	@Repeating(repetition = 10)
	@Concurrent(count = 5)
	public void decrementCounterTest() {
		counter.getAndDecrement();
		System.out.println("B " + Thread.currentThread().getName());

	}

	@AfterClass()
	public static void after() {
		assertThat(counter.get(), is(0));
	}
}
