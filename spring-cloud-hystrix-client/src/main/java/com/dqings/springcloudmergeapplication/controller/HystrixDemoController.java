package com.dqings.springcloudmergeapplication.controller;

import com.netflix.hystrix.HystrixCommandGroupKey;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class HystrixDemoController {

    @GetMapping("hello-world")
    @HystrixCommand(fallbackMethod = "fault",
            commandProperties =
                    {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "100")})
    public String helloWorld() throws InterruptedException {
        Random random = new Random();
        int anInt = random.nextInt(200);
        System.out.println("方法执行时间:"+anInt);
        Thread.sleep(anInt);
        return "hello,world";
    }



    public String fault(){
        return "Fault";
    }

    @GetMapping("hello-world-2")
    public String helloWorld2(){
        return new HelloWorldCommnd().execute();
    }

    private class HelloWorldCommnd extends com.netflix.hystrix.HystrixCommand<String> {

        protected HelloWorldCommnd() {
            super(HystrixCommandGroupKey.Factory.asKey("helloWorld"),100);
        }

        @Override
        protected String run() throws Exception {
            Random random = new Random();
            int anInt = random.nextInt(200);
            System.out.println("方法执行时间:"+anInt);
            Thread.sleep(anInt);
            return "hello,world";
        }

        @Override
        protected String getFallback() {
            return HystrixDemoController.this.fault();
        }
    }


}
