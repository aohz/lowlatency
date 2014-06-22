package com.distruptor;

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

	public TransactionProcessor() {
	}

	public void postTransaction(Transaction transaction) {

	}

	public void init() {
	}

	public void destroy() {
	}

}
