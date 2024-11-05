package ru.kata.spring.boot_security.demo.service;

import ru.kata.spring.boot_security.demo.entity.User;

import java.util.List;

public interface UserDetailsServ {
    User findUserById(Long userId);
    List<User> allUsers();
    void delete(Long id);
    void save(User user);
    void update(User existingUser, User user);
}
