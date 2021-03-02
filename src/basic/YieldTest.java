package basic;

public class YieldTest implements Runnable {
    @Override
    public void run() {
        Thread.yield();
    }

    public static void main(String[] args) {
        YieldTest yt = new YieldTest();
        Thread thread = new Thread(yt);
        thread.start();
    }
}
