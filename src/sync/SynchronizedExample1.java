package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 执行两个线程，调用的是同一个对象的同步代码块，因此这两个线程会进行同步，
 * 当一个线程进入同步代码块时，另一个线程就必须进行等待。
 */

public class SynchronizedExample1 {

    public void func() {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                System.out.println(i + " ");
            }
        }
    }

    public static void main(String[] args) {
        SynchronizedExample1 e = new SynchronizedExample1();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> e.func());
        executorService.execute(() -> e.func());
    }
}
