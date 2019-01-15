package com.dqings.springcloudmergeapplication;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.config.server.EnableConfigServer;

@SpringBootApplication
@EnableConfigServer
public class SpringCloudConfigServerApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(SpringCloudConfigServerApplication.class)
                .run(args);
    }

}
