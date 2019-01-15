package com.dqings.springcloudmergeapplication.demo;

import java.util.Observable;
import java.util.Observer;

public class ObserverDemo {

    public static void main(String[] args) {

        Observable observable = new MyObServerable();
        observable.addObserver(new Observer() {
            @Override
            public void update(Observable o, Object arg) {
                System.out.println("收到消息:"+arg);
            }
        });
        ((MyObServerable) observable).setChanged();
       observable.addObserver((observable1,value)->{
           System.err.println("收到消息:"+value);
        });
        ((MyObServerable) observable).setChanged();
       observable.notifyObservers("hello,world");

    }

    static class MyObServerable extends Observable{
        @Override
        protected synchronized void setChanged() {
            super.setChanged();
        }
    }


}
