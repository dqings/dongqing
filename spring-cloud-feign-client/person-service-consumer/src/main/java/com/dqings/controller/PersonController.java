package com.dqings.controller;

import com.dqings.person.api.PersonService;
import com.dqings.person.domain.Person;
import com.dqings.service.PersonServiceProxy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PersonController{

    @Autowired
    private PersonServiceProxy personServiceProxy;

    @PostMapping(value = "/person/save")
    public boolean save(@RequestBody Person person){
        return personServiceProxy.save(person);
    }

    @GetMapping(value = "/person/all")
    public Collection<Person> findAll(){
        return personServiceProxy.findAll();
    }

}
