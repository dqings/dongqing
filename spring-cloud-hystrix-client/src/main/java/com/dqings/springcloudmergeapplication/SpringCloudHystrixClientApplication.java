package com.dqings.springcloudmergeapplication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.hystrix.EnableHystrix;

@SpringBootApplication
//@EnableHystrix
@EnableCircuitBreaker
public class SpringCloudHystrixClientApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringCloudHystrixClientApplication.class,args);
    }

}
