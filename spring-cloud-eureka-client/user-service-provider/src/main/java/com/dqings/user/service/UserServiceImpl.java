package com.dqings.user.service;

import com.dqings.user.api.UserService;
import com.dqings.user.domain.User;
import com.dqings.user.repostory.UserRepostory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
public class UserServiceImpl implements UserService {


    private final UserRepostory userRepostory;

    @Autowired
    public UserServiceImpl(UserRepostory userRepostory) {
        this.userRepostory = userRepostory;
    }

    @Override
    public boolean save(User user) {
        return userRepostory.save(user);
    }

    @Override
    public Collection<User> findAll() {
        return userRepostory.findAll();
    }
}
