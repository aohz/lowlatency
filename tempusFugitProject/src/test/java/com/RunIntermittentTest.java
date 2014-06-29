package com;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.concurrent.atomic.AtomicInteger;

import org.junit.AfterClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.google.code.tempusfugit.concurrency.IntermittentTestRunner;
import com.google.code.tempusfugit.concurrency.annotations.Intermittent;

@RunWith(IntermittentTestRunner.class)
@Intermittent(repetition = 10)
public class RunIntermittentTest {
	
	private static final AtomicInteger counter = new AtomicInteger();

	@Test	
	public void incrementCounterTest() {
		counter.getAndIncrement();
		System.out.println("A " + Thread.currentThread().getName());

	}

	@Test	
	public void decrementCounterTest() {
		counter.getAndDecrement();
		System.out.println("B " + Thread.currentThread().getName());

	}
	
	@AfterClass()
	public static void after() {
		assertThat(counter.get(), is(0));
	}
}
