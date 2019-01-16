package com.dqings.user.service;

import com.dqings.user.api.UserService;
import com.dqings.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collection;

@Service
public class UserServiceProxy implements UserService {

    private final RestTemplate restTemplate;

    private static final String USER_SERVICE_PREFIX_URL="http://user-service-provider";

    @Autowired
    public UserServiceProxy(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public boolean save(User user) {
        return restTemplate.postForObject(USER_SERVICE_PREFIX_URL+"/user/save",user,Boolean.class);
    }

    @Override
    public Collection<User> findAll() {
        return restTemplate.getForObject(USER_SERVICE_PREFIX_URL+"/user/list",Collection.class);
    }
}
