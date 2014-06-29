package com;

import static com.google.code.tempusfugit.temporal.Duration.seconds;
import static com.google.code.tempusfugit.temporal.WaitFor.waitOrTimeout;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeoutException;

import org.junit.Test;

import com.google.code.tempusfugit.temporal.Condition;
import com.google.code.tempusfugit.temporal.Timeout;
/**
 * 
 * @author hernalb
 *
 */
public class WaitForTest {

	
	
	private final ArrayBlockingQueue<Integer> messages = new ArrayBlockingQueue<Integer>(
			10);

	/**
	 * Test that messages has been filled up or return after 5 seconds 
	 * 
	 * @throws TimeoutException
	 * @throws InterruptedException
	 */
	@Test
	public void testProducer() throws TimeoutException, InterruptedException {				
		new Thread(new Producer(messages)).start();
		waitOrTimeout(messagesIsFull(), Timeout.timeout(seconds(20)));	
	}

	private Condition messagesIsFull() {
		return new Condition() {
			public boolean isSatisfied() {
				return messages.remainingCapacity() == 0;
			}
		};
	}

}

/**
 * Class to test
 * This class adds random integers to the messages collection until it is full
 * 
 * @author hernalb
 *
 */
class Producer implements Runnable {

	private final BlockingQueue<Integer> messages;

	public Producer(BlockingQueue<Integer> messages) {
		super();
		this.messages = messages;
	}

	@Override
	public void run() {
		while (true) {
			try {
				messages.put(ThreadLocalRandom.current().nextInt());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
