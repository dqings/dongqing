package com.dqings.controller;

import com.dqings.person.domain.Person;
import com.dqings.service.PersonServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class PersonProviderController {

    @Autowired
    private PersonServiceImpl personServiceImpl;

    @PostMapping(value = "/person/save")
    public boolean savePerson(@RequestBody Person person){
        return personServiceImpl.savePerson(person);
    }

    @GetMapping(value = "/person/all")
    public Collection<Person> findAllPersons(){
        return personServiceImpl.findAllPersons();
    }

}
