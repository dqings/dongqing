package com.dqings.repository;

import com.dqings.person.domain.Person;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class PersonServiceRepository {

    private static final ConcurrentHashMap map = new ConcurrentHashMap();

    public boolean savePerson(Person person){
        return map.put(person.getId(),person)==null;
    }


    public Collection<Person> findAllPersons(){
        return map.values();
    }

}
