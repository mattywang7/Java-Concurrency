package usethread;

public class MyRunnable implements Runnable{

    @Override
    public void run() {
        System.out.println("This is thread MyRunnable.");
    }

    public static void main(String[] args) {
        MyRunnable instance = new MyRunnable();
        Thread thread = new Thread(instance);
        thread.start();
    }
}
