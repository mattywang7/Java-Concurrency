package interrupt;

/**
 * 在main()中启动一个线程之后再中断它，由于线程中调用了Thread.sleep()方法，
 * 因此会抛出一个InterruptedException, 从而提前结束线程，不执行之后的语句。
 */

public class InterruptExample {

    private static class MyThread1 extends Thread {
        @Override
        public void run() {
            try {
                Thread.sleep(2000);
                System.out.println("Thread run");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private static class MyThread2 extends Thread {
        @Override
        public void run() {
            while (!interrupted()) {
                System.out.println("Still running...");
            }
            System.out.println("Thread end.");
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread thread2 = new MyThread2();
        thread2.start();
        thread2.interrupt();
    }
}
