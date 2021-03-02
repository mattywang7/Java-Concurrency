package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedExample5 {

    public synchronized static void func() {
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
        }
    }

    public void func2() {}

    public static void main(String[] args) {
        SynchronizedExample5 e1 = new SynchronizedExample5();
        SynchronizedExample5 e2 = new SynchronizedExample5();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> e1.func());
        executorService.execute(() -> e2.func());
    }
}
