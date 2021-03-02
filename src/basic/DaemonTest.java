package basic;

import usethread.MyRunnable;

public class DaemonTest {

    public static void main(String[] args) {
        Thread thread = new Thread(new MyRunnable());
        thread.setDaemon(true);
    }
}
