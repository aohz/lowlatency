package com.examples.collections;

import java.util.Date;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
/**
 * This example shows the use of a bounded blocking queue that is backed by
 * an array. In this example we have an immutable SimpleAddition class that
 * is passed from the producer to the consumer, we generate random numbers
 * using the Random class that form the basis for the addition.
 */
public class ArrayBlockingQueueExample {
    // the maximum number of items that can be on the queue
    private static final int MAX_CAPACITY = 100;
    // the number of items to queue, make larger than capacity
    private static final int ITEMS_TO_QUEUE = 250;
    // create a bounded queue based on an array.
    private BlockingQueue<SimpleAddition> queue =
            new ArrayBlockingQueue<SimpleAddition>(MAX_CAPACITY);
    // a latch that counts down the items we have processed
    private CountDownLatch consumeLatch = new CountDownLatch(ITEMS_TO_QUEUE);
    // and a random number generator for creating addition instances
    private Random random = new Random(System.currentTimeMillis());
    
    public static void main(String[] args) throws InterruptedException {
        ArrayBlockingQueueExample example = new ArrayBlockingQueueExample();
        example.init();
    }
    
    public void init() throws InterruptedException {
        // run the consumer thread, there could be more than one consumer
        Thread th = new Thread(new QueueConsumerProcessor(), "QueueProcessor");
        th.setDaemon(true); // don't hold the VM open for this thread
        th.start();
        // and load the queue with some values, should the queue fill
        // up, the put call will block.
        for(int i=0; i<ITEMS_TO_QUEUE;i++) {
            SimpleAddition addition = new SimpleAddition(
                        random.nextInt(128), random.nextInt(128)
            );
            queue.put(addition);
            TimeUnit.SECONDS.sleep(2);
        }
        // This latch counts down each event in the producer, and once
        // all the events are processed we exit the VM.
        consumeLatch.await();
    }
    /**
     * This runnable simulates the real world producer, it takes items of
     * work from the queue and processes them. In a real world system
     * there could well be more than one processor thread.
     */
    private class QueueConsumerProcessor implements Runnable {
        @Override
        public void run() {
            try {
                while(!Thread.currentThread().isInterrupted()) {
                    // attempt to take the next work item off the queue
                    // if we consume quicker than the producer then take
                    // will block until there is work to do.                	
                	SimpleAddition addition = queue.take();
                    // and now do the work!
                    int result = addition.getN1() + addition.getN2();
                    System.out.printf(new Date() + " Addition: %d + %d = %d", addition.getN1(), addition.getN2(), result);
                    System.out.println();
                    // decrement the countdown latch
                    consumeLatch.countDown();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                // no need to re set interrupted as we are outside loop
            }
        }
    }
    /**
     * This immutable object is used to pass the state over to the queue
     * processor. Made a static inner class to avoid the overhead of
     * access back to the instance.
     */
    private final static class SimpleAddition {
        private final int n1;
        private final int n2;
        public SimpleAddition(int n1, int n2) {
            this.n1 = n1;
            this.n2 = n2;
        }
        private int getN1() {
            return n1;
        }
        private int getN2() {
            return n2;
        }
    }
}
