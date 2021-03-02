package cooperation;

/**
 * 虽然b线程先启动，但是因为在b线程中调用了a线程的join(),
 * b线程会等待a线程结束才继续执行，
 * 因此最后能够保证a线程的输出先于b线程的输出。
 */

public class JoinExample {

    private class A extends Thread {
        @Override
        public void run() {
            System.out.println("A");
        }
    }

    private class B extends Thread {
        private A a;

        public B(A a) {
            this.a = a;
        }

        @Override
        public void run() {
            try {
                a.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("B");
        }
    }

    public void test() {
        A a = new A();
        B b = new B(a);
        b.start();  // b starts first
        a.start();
    }

    public static void main(String[] args) {
        JoinExample je = new JoinExample();
        je.test();  // print A first
    }
}
