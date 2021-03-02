package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedExample4 {

    public void func() {
        synchronized (SynchronizedExample4.class) {
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        }
    }

    public static void main(String[] args) {
        SynchronizedExample4 e1 = new SynchronizedExample4();
        SynchronizedExample4 e2 = new SynchronizedExample4();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> e1.func());
        executorService.execute(() -> e2.func());
    }
}
