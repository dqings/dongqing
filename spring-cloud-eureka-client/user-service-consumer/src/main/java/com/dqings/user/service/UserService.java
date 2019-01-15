package com.dqings.user.service;

import com.dqings.user.domain.User;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserService implements com.dqings.user.api.UserService {

    @Override
    public boolean save(User user) {
        return false;
    }

    @Override
    public Collection<User> findAll() {
        return null;
    }
}
