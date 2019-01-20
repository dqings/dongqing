package com.dqings.person.hystrix;

import com.dqings.person.api.PersonService;
import com.dqings.person.domain.Person;

import java.util.Collection;
import java.util.Collections;

public class PersonServiceFallback implements PersonService {

    @Override
    public boolean save(Person person) {
        return false;
    }

    @Override
    public Collection<Person> findAll() {
        return Collections.emptyList();
    }
}
