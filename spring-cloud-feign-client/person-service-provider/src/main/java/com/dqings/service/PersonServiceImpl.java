package com.dqings.service;

import com.dqings.person.domain.Person;
import com.dqings.repository.PersonServiceRepository;
import com.netflix.discovery.converters.Auto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class PersonServiceImpl {

    @Autowired
    private PersonServiceRepository personServiceRepository;

    public boolean savePerson(Person person){
        return personServiceRepository.savePerson(person);
    }


    public Collection<Person> findAllPersons(){
        return personServiceRepository.findAllPersons();
    }

}
