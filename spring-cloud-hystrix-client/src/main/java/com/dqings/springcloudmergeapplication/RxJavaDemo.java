package com.dqings.springcloudmergeapplication;

import rx.Observer;
import rx.Single;
import rx.schedulers.Schedulers;

import java.util.Random;

public class RxJavaDemo {

    public static void main(String[] args) {
        Single.just("hello,world")
                .subscribeOn(Schedulers.immediate())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {
                        System.out.println("执行结束");
                    }

                    @Override
                    public void onError(Throwable e) {
                        System.out.println("超时保护");
                    }

                    @Override
                    public void onNext(String s) {
                        Random random = new Random();
                        int anInt = random.nextInt(200);
                        System.out.println("方法执行时间:"+anInt);
                        if(anInt>100) throw new RuntimeException("time out");
                        System.out.println(s);
                    }
                });
    }

}
