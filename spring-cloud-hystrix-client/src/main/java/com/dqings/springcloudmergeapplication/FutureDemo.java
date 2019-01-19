package com.dqings.springcloudmergeapplication;

import java.util.Random;
import java.util.concurrent.*;

public class FutureDemo {

    private static final Random random = new Random();
    private static final ExecutorService executor = Executors.newFixedThreadPool(1);

    public static void main(String[] args) {
        Future<String> future = executor.submit(() -> {
            Random random = new Random();
            int anInt = random.nextInt(200);
            System.out.println("方法执行时间:" + anInt);
            Thread.sleep(anInt);
            return "hello,world";
        });
        try {
            String s = future.get(100, TimeUnit.MILLISECONDS);
            System.out.println(s);
        } catch (Exception e) {
            System.out.println("超时保护");
        }
        executor.shutdown();
    }

}
