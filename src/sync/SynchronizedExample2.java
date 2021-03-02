package sync;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 两个线程调用了不同对象的代码块，因此这两个线程就不需要进行同步，两个线程交叉运行。
 */

public class SynchronizedExample2 {

    public void func() {
        synchronized (this) {
            for (int i = 0; i < 10; i++) {
                System.out.print(i + " ");
            }
        }
    }

    public static void main(String[] args) {
        SynchronizedExample2 e1 = new SynchronizedExample2();
        SynchronizedExample2 e2 = new SynchronizedExample2();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(e1::func);
        executorService.execute(e2::func);
    }
}
