package safe;

/**
 * thread1 设置 threadLocal 为 1，而 thread2 设置 threadLocal 为 2。
 * 过了一段时间之后，thread1 读取 threadLocal 依然是 1，不受 thread2 的影响。
 */

public class ThreadLocalExample {

    public static void main(String[] args) {
        ThreadLocal threadLocal = new ThreadLocal();
        Thread thread1 = new Thread(() -> {
            threadLocal.set(1);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(threadLocal.get());
            threadLocal.remove();
        });

        Thread thread2 = new Thread(() -> {
            threadLocal.set(2);
            threadLocal.remove();
        });
        thread1.start();
        thread2.start();
    }
}
