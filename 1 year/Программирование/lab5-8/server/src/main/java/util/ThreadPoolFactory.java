package util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ForkJoinPool;

public class ThreadPoolFactory {
    private static ExecutorService fixedThreadPool = Executors.newFixedThreadPool(10);
    private static ForkJoinPool forkJoinPool = new ForkJoinPool(10);

    public static ExecutorService getFixedThreadPool() {
        return fixedThreadPool;
    }

    public static void setFixedThreadPool(ExecutorService fixedThreadPool) {
        ThreadPoolFactory.fixedThreadPool = fixedThreadPool;
    }

    public static ForkJoinPool getForkJoinPool() {
        return forkJoinPool;
    }

    public static void setForkJoinPool(ForkJoinPool forkJoinPool) {
        ThreadPoolFactory.forkJoinPool = forkJoinPool;
    }
}
