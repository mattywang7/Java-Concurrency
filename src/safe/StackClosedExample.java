package safe;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class StackClosedExample {

    public void add100() {
        int cnt = 0;
        for (int i = 0; i < 100; i++) {
            cnt++;
        }
        System.out.println(cnt);
    }

    public static void main(String[] args) {
        StackClosedExample stackClosedExample = new StackClosedExample();
        ExecutorService executorService = Executors.newCachedThreadPool();
        executorService.execute(() -> {
            stackClosedExample.add100();
        });
        executorService.execute(() -> {
            stackClosedExample.add100();
        });
        executorService.shutdown();
    }
}
