# 一、使用线程

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

# 二、基础线程机制

## Executor

Executor管理多个异步任务的执行，而无需程序员显示地管理线程的生命周期。
这里的异步是指多个任务的执行不受干扰，不需要进行同步操作。

主要有3中Executor:
- CachedThreadPool: 一个任务创建一个线程
- FixedThreadPool: 所有任务只能使用固定大小的线程
- SingleThreadExecutor: 相当于大小为1的FixedThreadPool

## Daemon

守护线程是程序运行时在后台提供服务的线程，不属于程序中不可或缺的部分。
当所有非守护线程结束时，程序也就终止，同时会杀死所有守护线程。
main()属于非守护线程。
在线程启动之前使用setDaemon()可以将一个线程设置为守护线程。

## sleep()

`Thread.sleep(millisec)` 会休眠当前正在执行的线程。
`sleep()`可能会抛出`InterruptedException`, 因为异常不能跨线程传播回main()中，因此必须在本地进行处理。
线程中抛出的其他异常也同样需要在本地进行处理。

## yield()

对静态方法Thread.yield()的调用声明了当前线程已经完成了生命周期中最重要的部分，可以切换给其他线程来执行。
该方法只是对线程调度器的一个建议，而且也只是建议具有相同优先级的其他线程可以运行。

# 三、中断

一个线程执行完毕之后会自动结束，如果在运行过程中发生异常也会提前结束。

## InterruptedException

通过调用一个线程的interrupt()来中断该线程，如果该线程处于阻塞、限期等待或者无限期等待状态，那么就会抛出InterruptedException，
从而提前结束该线程，不执行之后的语句。

## interrupted()

如果一个线程的run()方法执行一个无限循环，并且没有执行sleep()等会抛出InterruptedException的操作，那么调用线程的interrupt()方法就无法使线程提前结束。
但是调用interrupt()会设置线程的中断标记，此时调用interrupted()会返回true。
因此可以在循环体中使用interrupted()来判断线程是否处于中断状态，从而提前结束线程。

## Executor的中断操作

调用Executor的shutdown()方法会等待线程都执行完毕之后再关闭，但是如果调用的是shutdownNow()，则相当于调用每个线程的interrupt()方法。

如果只想中断Executor中的一个线程，可以通过使用submit()来提交一个线程，它会返回一个`Future<?>`对象，通过调用该对象`cancel(true)`方法就可以中断该线程。

# 四、互斥同步

Java提供了两种锁机制来控制多个线程对共享资源的互斥访问，第一个是JVM实现的`synchronized`, 而另一个是JDK实现的`ReentrantLock`.

## synchronized

### 同步一个代码块：
```
public void func() {
    synchronized (this) {
        // ...
    }
}
```
它只作用同一个对象，如果调用两个对象上的同步代码块，就不会进行同步。

### 同步一个方法：
```
public synchronized void func() {
    // ...
}
```
和同步代码块一样，作用于同一个对象。

### 同步一个类
```
public void func() {
    synchronized (SynchronizedExample.class) {
        // ...
    }
}
```
作用于一整个类，也就是说两个线程调用同一个类的不同对象上的这种同步语句，也会进行同步。

### 同步一个静态方法
```
public synchronized static void func() {
    // ...
}
```
作用于整个类

## ReentrantLock

ReentrantLock是`java.util.concurrent`中的锁

## 比较
- synchronized 是JVM实现的，而ReentrantLock是JDK实现的
- 新版本Java对synchronized进行了很多优化，例如自旋锁等，synchronized与而ReentrantLock性能大致相同
- 当持有锁的线程长期不释放锁的时候，正在等待的线程可以选择放弃等待，改为处理其他事情。
ReentrantLock可中断，synchronized则不行。
  
- 公平锁是指多个线程在等待同一个锁时，必须按照申请锁的时间顺序来依次获得锁
  synchronized的锁是非公平的，ReentrantLock默认情况下也是非公平的，但是也可以是公平的。
  
- 一个ReentrantLock可以同时绑定多个Condition对象

## 使用选择
除非需要使用ReentrantLock的高级功能，否则优先使用synchronized。
这是因为synchronized是JVM实现的一种锁机制，JVM原生地支持它，而 ReentrantLock 不是所有的JDK版本都支持。
并且使用 synchronized 不用担心没有释放锁而导致死锁问题，因为JVM会确保锁的释放。

# 五、线程之间的协作
当多个线程可以一起工作去解决某个问题时，如果某些部分必须在其它部分之前完成，那么就需要对线程进行协调。

## join()
在线程中调用另一个线程的join(), 会将当前线程挂起，而不是忙等待，直到目标线程结束。

## wait(), notify(), and notifyAll()
调用 wait() 使得线程等待某个条件满足，线程在等待时会被挂起，当其它线程的运行使得这个条件满足时，
其它线程会调用 notify() 或者 notifyAll() 来唤醒挂起的线程。

它们都属于 Object 的一部分，而不属于Thread.

只能用在同步方法或者同步控制块中，否则会在运行时抛出 IllegalMonitorStateException.

使用 wait() 挂起期间，线程会释放锁。
这是因为，如果没有释放锁，那么其它线程就无法进入对象的同步方法或者同步控制块中，
那么就无法执行 notify() 或者 notifyAll() 来唤醒等待的线程，造成死锁。

## wait() VS sleep()
- wait() 是 Object 的方法，而 sleep() 是 Thread 的静态方法。
- wait() 会释放锁，而 sleep() 不会。

## await(), signal(), and signalAll()
`java.util.concurrent` 提供了 Condition 类来实现线程之间的协调，
可以在 Condition 上调用 await() 是线程等待，其它线程调用 signal() 或者 signalAll() 方法唤醒等待的线程。

相比于 wait(), await() 可以指定等待的条件，因此更加灵活。

# 六、线程状态
一个线程只能处于一种状态，并且这里的线程状态特质 Java 虚拟机的线程状态，不能反映线程在特定操作系统下的状态。

## 新建 (New)
创建后尚未启动。

## 可运行 (Runnable)
正在 Java 虚拟机中运行。
但是在操作系统层面，也可能等待资源调度（例如处理器资源），资源调度完成后就进入运行状态。
所以该状态的可运行是指可以被运行，具体有没有运行要看底层操作系统的资源调度。

## 阻塞 (Block)
请求获取 monitor lock 从而进入 synchronized 函数或者代码块，但是其它线程已经占用了该 monitor lock，所以出于阻塞状态。
要结束该状态进入从而 Runnable 需要其他线程释放 monitor lock。

## 无限期等待 (Waiting)
等待其它线程显示地唤醒。
阻塞和等待的区别在于，阻塞是被动的，它是在等待获取 monitor lock。
而等待是主动的，通过调用 Object.wait() 等方法进入该状态。

| 进入方法 | 退出方法 |
| --- | --- |
| 没有设置 Timeout 参数的 Object.wait() | {Object.notify() or Object.notifyAll()} by other threads |
| 没有设置 Timeout 参数的 Thread.join() | 被调用的线程执行完毕 |
| LockSupport.park() | LockSupport.unpack(Thread) |

## 限期等待 (Timed_waiting)
无需等待其它线程显示地唤醒，在一定时间之后会被系统自动唤醒。

| 进入方法 | 退出方法|
| --- | --- |
| Thread.sleep() | 时间结束 |
| 设置了 Timeout 参数的 Object.wait() | 时间结束 or Object.notify() or Object.notifyAll() |
| 设置了 Timeout 参数的 Thread.join() | 时间结束 or 被调用的线程执行完毕 |
| LockSupport.parkNanos() | LockSupport.unpack(Thread) |
| LockSupport.parkUntil() | LockSupport.unpack(Thread) |

调用 Thread.sleep() 方法使线程进入限期等待状态时，常常用“使一个线程睡眠”进行描述。
调用 Object.wait() 方法使线程进入限期等待或者无限期等待时，常常用“挂起一个线程”进行描述。
睡眠和挂起是用来描述行为，而阻塞和等待用来描述状态。

## 死亡 (Terminated)
可以是线程结束任务之后自己结束，或者产生了异常结束。

# J.U.C - AQS

`java.util.concurrent` 大大提升了并发性能，AQS被认为是 J.U.C 的核心。

## CountDownLatch
用来控制一个线程或多个线程等待多个线程.
维护了一个计数器cnt, 每次调用countDown()会让计数器的值减1， 减到0的时候，那些因为调用await()而在等待的线程就会被唤醒。

## CyclicBarrier

用来控制多个线程互相等待，只有当多个线程都到达时，这些线程才会继续执行。
和 CountDownLatch 类似，都是通过维护计数器来实现的。
线程执行 await() 之后计数器会减1，并进行等待，直到计数器为0，所有调用 await() 而在等待的线程才能继续执行。

CyclicBarrier 和 CountDownLatch 的一个区别是，CyclicBarrier 的计数器通过调用 reset() 可以循环使用，所以它才叫循环屏障。

CyclicBarrier 有两个构造函数，其中 parties 只是计数器的初始值，barrierAction 在所有线程都到达屏障的时候会执行一次。
```java
public class CyclicBarrier {
    
    public CyclicBarrier(int parties, Runnable barrierAction) {
        // ...
    }
    
    public CyclicBarrier(int parties) {
        this(parties, null);
    }
}
```

## Semaphore
Semaphore 类似于操作系统中的信号量，可以控制互斥资源的访问线程数。