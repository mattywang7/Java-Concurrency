package basic;

public class SleepTest implements Runnable {
    @Override
    public void run() {
        try {
            Thread.sleep(3000);
            System.out.println("After 3 seconds.");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SleepTest st = new SleepTest();
        Thread thread = new Thread(st);
        thread.start();
    }
}
