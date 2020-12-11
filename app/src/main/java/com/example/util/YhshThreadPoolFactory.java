package com.example.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Android Studio.
 * User: HuangWeiQiang
 * Date: 2020/8/20
 * Time: 17:04
 */
public class YhshThreadPoolFactory {

    private static ExecutorService mThreadService = Executors.newSingleThreadExecutor();

    public static class Alone {
        private static final YhshThreadPoolFactory yhshThreadPoolFactory = new YhshThreadPoolFactory();
    }

    public static YhshThreadPoolFactory getInstance() {
        return Alone.yhshThreadPoolFactory;
    }

    public <T> Future<T> submitRequest(Runnable runnable, T result) {
        return mThreadService.submit(runnable, result);
    }

    public void executeRequest(Runnable runnable) {
        if (mThreadService == null) mThreadService = Executors.newSingleThreadExecutor();
        mThreadService.execute(runnable);

    }

    public static boolean IsShutDown() {
        return mThreadService.isShutdown();
    }

    public void shutDown() {
        mThreadService.shutdownNow();
        mThreadService = null;
    }
}
