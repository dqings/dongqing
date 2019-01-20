package com.dqings.service;

import com.dqings.person.api.PersonService;
import com.dqings.person.domain.Person;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PersonServiceProxy implements PersonService {

    private final PersonService personService;

    public PersonServiceProxy(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public boolean save(Person person) {
        return personService.save(person);
    }

    @Override
    public Collection<Person> findAll() {
        return personService.findAll();
    }
}
