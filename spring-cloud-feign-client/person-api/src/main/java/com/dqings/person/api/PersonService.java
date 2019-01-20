package com.dqings.person.api;

import com.dqings.person.domain.Person;
import com.dqings.person.hystrix.PersonServiceFallback;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Collection;

@FeignClient(value = "person-service-provider",fallback = PersonServiceFallback.class)
public interface PersonService {

    @PostMapping(value = "/person/save")
    boolean save(@RequestBody  Person person);

    @GetMapping(value = "/person/all")
    Collection<Person> findAll();

}
