package com.dqings.user.web.controller;

import com.dqings.user.api.UserService;
import com.dqings.user.domain.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collection;

@RestController
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("user/save")
    public boolean save(@RequestBody User user){
        if(userService.save(user)){
            System.out.println("保存用户成功:"+user);
        }
        return false;
    }

    @GetMapping("user/list")
    public Collection<User> getAll(){
        return userService.findAll();
    }
}
