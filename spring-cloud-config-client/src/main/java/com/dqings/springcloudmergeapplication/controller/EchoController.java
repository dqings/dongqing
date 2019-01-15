package com.dqings.springcloudmergeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.context.refresh.ContextRefresher;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RestController
@RefreshScope
public class EchoController {

    private final ContextRefresher contextRefresher;
    private final Environment environment;

    @Value("${name}")
    private String name;

    @Autowired
    public EchoController(ContextRefresher contextRefresher, Environment environment) {
        this.contextRefresher = contextRefresher;
        this.environment = environment;
    }

    @GetMapping("name")
    public String getName(){
        return name;
    }

    @Scheduled(fixedRate = 5*1000,initialDelay = 3*1000)
    public void autoRefresh(){
        Set<String> stringSet = contextRefresher.refresh();
        stringSet.forEach(name ->{
            System.err.printf("[Thread:%s],服务端配置已更新,name=%s,value=%s\n",
                    Thread.currentThread().getName(),
                    name,
                    environment.getProperty(name));
        });
    }

}
