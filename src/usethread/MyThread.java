package usethread;

public class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Extends from Thread.");
    }

    public static void main(String[] args) {
        MyThread mt = new MyThread();
        mt.start();
    }
}
