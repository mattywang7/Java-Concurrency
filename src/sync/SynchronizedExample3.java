package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SynchronizedExample3 {

    public synchronized void func() {
        for (int i = 0; i < 10; i++) {
            System.out.print(i + " ");
        }
    }

    public static void main(String[] args) {
        SynchronizedExample3 e1 = new SynchronizedExample3();
        SynchronizedExample3 e2 = new SynchronizedExample3();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(e1::func);
        executorService.execute(e2::func);
    }
}
