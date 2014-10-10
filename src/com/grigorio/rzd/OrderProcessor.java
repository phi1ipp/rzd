package com.grigorio.rzd;

import java.util.concurrent.BlockingQueue;

/** Class which processes orders from a queue,
 * i.e. take an order out, make a request to WS and delivers a result to a client
 * Created by philipp on 10/10/14.
 */

public class OrderProcessor implements Runnable {
    private final BlockingQueue<Order> queue;

    OrderProcessor(BlockingQueue q) {
        queue = q;
    }

    @Override
    public void run() {
        System.out.println("Processor thread started");
        while (true) {
            try {
                Order ord = queue.take();

                if (ord.getId() == 0) {
                    System.out.println("Signal to exit!");
                    break;
                } else {
                    System.out.println("New order to process, id=" + ord.getId());
                }
            } catch (InterruptedException e) {
                System.err.println("Interrupted... Exiting!");
                return;
            }
        }
        System.out.println("Processor thread finished");
    }
}
