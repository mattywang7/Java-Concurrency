# 使用线程

有3种使用线程的方法
- 实现Runnable接口
- 实现Callable接口
- 继承Thread类

实现Runnable和Callable接口的类只能当作一个可以在线程中运行的任务，不是真正意义上的线程，因此最后还需要通过Thread来调用。
可以理解为任务是通过线程驱动从而执行的。

## Runnable

实现接口的run():
```java
public class MyRunnable implements Runnable {
    @Override
    public void run() {
        // ...
    }
}
```
使用Runnable实例再创建一个Thread实例，然后调用Thread实例的start()来启动线程

## Callable

与Runnable相比，Callable可以有返回值，返回值通过FutureTask进行封装

## 继承Thread

同样也是需要实现run(), 因为Thread也实现了Runnable接口
当调用start()启动；一个线程时，虚拟机会将该线程放入就绪队列中等待被调度，当一个线程被调度时会执行该线程的run()

## 实现接口 vs 继承Thread

实现接口会更好一些，因为：
- Java不支持多重继承，因此继承了Thread类就无法继承其他类，但是可以实现多个接口
- 类可能只要求可执行就行，继承整个Thread类开销过大