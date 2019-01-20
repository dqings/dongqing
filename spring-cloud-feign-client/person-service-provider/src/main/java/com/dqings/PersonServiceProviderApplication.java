package com.dqings;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class PersonServiceProviderApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonServiceProviderApplication.class,args);
    }
}
