package jucother;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 使用 BlockingQueue 实现生产者消费者问题。
 */

public class ProducerConsumer {
    private static BlockingQueue<String> queue = new ArrayBlockingQueue<>(5);

    private static class Producer extends Thread {
        @Override
        public void run() {
            try {
                queue.put("Product");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("produced");
        }
    }

    private static class Consumer extends Thread {
        @Override
        public void run() {
            try {
                String product = queue.take();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("consumed");
        }
    }

    /**
     * 保持生产者线程数 ≥ 消费者线程数，并且 (produce - consumer) ≤ capacity
     */
    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            Producer producer = new Producer();
            producer.start();
        }

        for (int i = 0; i < 5; i++) {
            Consumer consumer = new Consumer();
            consumer.start();
        }

        for (int i = 0; i < 3; i++) {
            Producer producer = new Producer();
            producer.start();
        }
    }
}
