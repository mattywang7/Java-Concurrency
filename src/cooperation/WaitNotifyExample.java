package cooperation;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class WaitNotifyExample {

    public synchronized void before() {
        System.out.println("before");
        notifyAll();    // 通知其它所有线程，该线程已经执行完毕
    }

    public synchronized void after() {
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("after");
    }

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        WaitNotifyExample wne = new WaitNotifyExample();
        executorService.execute(() -> wne.after());
        executorService.execute(() -> wne.before());
        executorService.shutdown(); // shutdown all tasks which are executed
    }
}
