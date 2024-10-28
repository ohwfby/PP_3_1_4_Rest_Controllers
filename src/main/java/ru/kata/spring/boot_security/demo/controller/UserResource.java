package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

@RestController
@RequestMapping("/api/v1/user")
public class UserResource {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserResource(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("/{userId}")
    public User findById(@PathVariable Long userId) {
        return userDetailsServiceImpl.findUserById(userId);
    }

    @PostMapping
    public void create(@RequestBody User user) {
        userDetailsServiceImpl.save(user);
    }

}
