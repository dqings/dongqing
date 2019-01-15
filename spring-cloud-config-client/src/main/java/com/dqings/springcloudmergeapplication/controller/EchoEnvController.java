package com.dqings.springcloudmergeapplication.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class EchoEnvController {

    private final Environment environment;

    @Value("${server.port}")
    private Integer port;

    @Autowired
    public EchoEnvController(Environment environment) {
        this.environment = environment;
    }

    @GetMapping("echo/env/{name}")
    public Map<String,String> environment(@PathVariable String name){
        Map<String,String> data = new HashMap<>();
        data.put(name,environment.getProperty(name));
        return data;
    }

}
