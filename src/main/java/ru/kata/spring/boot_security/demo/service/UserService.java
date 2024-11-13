package ru.kata.spring.boot_security.demo.service;


import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    List<User> getAllUsers();

    User getUserById(Long id);

    User saveUser(User existingUser, User user);

    void deleteUser(User user);

    User getUserByFirstname(String name);

    Optional<User> getUserByEmail(String email);
}
