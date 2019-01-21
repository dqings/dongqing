package com.dqings.controller;

import com.dqings.person.domain.Person;
import com.dqings.service.PersonServiceImpl;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;
import java.util.Collections;
import java.util.Random;

@RestController
public class PersonProviderController {

    @Autowired
    private PersonServiceImpl personServiceImpl;

    private static final Random random = new Random();

    @PostMapping(value = "/person/save")
    public boolean savePerson(@RequestBody Person person){
        return personServiceImpl.savePerson(person);
    }

    @GetMapping(value = "/person/all")
    @HystrixCommand(fallbackMethod = "findAllPersonsFallBack",
            commandProperties =
                    {@HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds",value = "100")})
    public Collection<Person> findAllPersons() throws InterruptedException {
        int anInt = random.nextInt(200);
        System.out.println("person 服务调用时间 : "+anInt);
        Thread.sleep(anInt);
        return personServiceImpl.findAllPersons();
    }


    public Collection<Person> findAllPersonsFallBack(){
        System.err.println("person服务超时");
        return Collections.emptyList();
    }

}
