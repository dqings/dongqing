package com.dqings.springcloudmergeapplication.demo;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class SpringEventListenerDemo {

    public static void main(String[] args) {

        //annotation 驱动的spring上下文
        AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext();
        //注册监听器
        context.addApplicationListener((applicationEvent)->{
            System.err.println("接收到事件:"+applicationEvent.getSource()+"@");
        });
        //启动上下文
        context.refresh();

        //发布事件

        context.publishEvent(new MyApplicationEvent("hello,world"));
        context.close();
    }

    static class MyApplicationEvent extends ApplicationEvent {



        public MyApplicationEvent(Object source) {
            super(source);
        }


    }

}
