package github.beeclimb.utils.concurrent.threadpool;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPoolFactoryUtilsTest {
    private static final int CORE_POOL_SIZE = 5;
    private static final int MAX_POOL_SIZE = 10;
    private static final int QUEUE_CAPACITY = 20;
    private static final Long KEEP_ALIVE_TIME = 1L;

    public static void main(String[] args) {
        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                CORE_POOL_SIZE,
                MAX_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(QUEUE_CAPACITY),
                new NamingThreadFactory("InsertPool"));

        for (int i = 0; i < 10; ++i) {
            Runnable worker = new MyRunnable("" + i);
            executor.execute(worker);
        }
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
    }
}

final class NamingThreadFactory implements ThreadFactory {
    private final AtomicInteger threadNum = new AtomicInteger(1);
    private final String name;

    public NamingThreadFactory(String name) {
        this.name = name;
    }

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setName(name + " [#" + threadNum.incrementAndGet() + "]");
        return thread;
    }
}

final class MyRunnable implements Runnable {
    private String command;

    public MyRunnable(String command) {
        this.command = command;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName() + " start. time=" + new Date());
        processCommand();
        System.out.println(Thread.currentThread().getName() + " end. time=" + new Date());
    }

    public void processCommand() {
        try {
            Thread.sleep(5000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

class ThreadPoolTest {
    public static void main(String[] args) {
        ExecutorService executorService = ThreadPoolFactoryUtils.createCustomThreadPoolIfAbsent("Update");
        for (int i = 0; i < 10; ++i) {
            Runnable worker = new MyRunnable("" + i);
            executorService.execute(worker);
        }
        ThreadPoolFactoryUtils.printThreadPoolStatus((ThreadPoolExecutor) executorService);
    }

    @Test
    public void shutdownTest() {
        ThreadPoolFactoryUtils.shutDownAllThreadPool();
    }
}