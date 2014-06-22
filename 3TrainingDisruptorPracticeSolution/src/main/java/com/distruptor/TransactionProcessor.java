package com.distruptor;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;






import com.distruptor.event.TransactionEvent;
import com.distruptor.handler.DemoWorkHandler;
import com.distruptor.handler.GenericExceptionHandler;
import com.distruptor.handler.JournalTransactionHandler;
import com.distruptor.handler.PostTransactionHandler;
import com.distruptor.handler.ReplicateTransactionHandler;
import com.distruptor.translator.TransactionEventPublisher;
import com.lmax.disruptor.BlockingWaitStrategy;
import com.lmax.disruptor.ExceptionHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import com.model.Transaction;

/**
 * <p>
 * Prototype based on the LMAX disruptor framework:
 * http://lmax-exchange.github.io/disruptor/
 * </p>
 * 
 * <p>
 * Inspired by this blog post:
 * http://blog.jteam.nl/2011/07/20/processing-1m-tps-
 * with-axon-framework-and-the-disruptor/
 * </p>
 * 
 * <p>
 * This is an extremely oversimplified version of the "diamond configuration" as
 * described in
 * http://mechanitis.blogspot.com/2011/07/dissecting-disruptor-wiring-up.html
 * </p>
 * 
 * <p>
 * In this implementation, a journal and replication step happen concurrently,
 * and both must succeed for the post step to occur, as shown below:
 * </p>
 * 
 * <pre>
 *          replicate
 *            /   \
 *           /     \
 *  event ->       post
 *           \     /
 *            \   /
 *           journal
 * </pre>
 * 
 * <p>
 * This is also an example of in-memory storage based on "event sourcing" - see
 * Martin Fowler: http://martinfowler.com/eaaDev/EventSourcing.html
 * </p>
 * 
 */
public class TransactionProcessor {

	private static final Logger logger = LoggerFactory
			.getLogger(TransactionProcessor.class);

	private final ExecutorService EXECUTOR = Executors.newCachedThreadPool();
	private Disruptor disruptor;
	private RingBuffer ringBuffer;

	private JournalTransactionHandler journal;
	private ReplicateTransactionHandler replicate;
	private PostTransactionHandler post;

	public void postTransaction(Transaction transaction) {
		disruptor.publishEvent(new TransactionEventPublisher(transaction));
		// if you don't want to use the event translator
		// long sequence = ringBuffer.next();
		// TransactionEvent te = (TransactionEvent) ringBuffer.get(sequence);
		// te.setTransaction(transaction);
		// disruptor.getRingBuffer().publish(sequence);
	}

	public void init() {
				
		disruptor = new Disruptor<TransactionEvent>(
				TransactionEvent.EVENT_FACTORY, 1024, EXECUTOR,
				ProducerType.MULTI, new BlockingWaitStrategy());
		
		journal = new JournalTransactionHandler();

		replicate = new ReplicateTransactionHandler();

		post = new PostTransactionHandler();
		
		// This is where the magic happens
		// (see "diamond configuration" in javadoc above)
		disruptor.handleEventsWith(journal, replicate).then(post);
		
//		disruptor.handleEventsWith(journal,
//		replicate).handleEventsWithWorkerPool(new DemoWorkHandler(), new
//		DemoWorkHandler()).then(post);

		// We don't do any fancy exception handling in this demo, but if we
		// did, one way to set it up for each handler is like this:
		ExceptionHandler exh = new GenericExceptionHandler();
		disruptor.handleExceptionsFor(journal).with(exh);
		disruptor.handleExceptionsFor(replicate).with(exh);		
		disruptor.handleExceptionsFor(post).with(exh);

		ringBuffer = disruptor.start();

	}

	public void destroy() {
		try {
			disruptor.shutdown();
		} catch (Exception ignored) {
		}

		EXECUTOR.shutdownNow();
	}

}
