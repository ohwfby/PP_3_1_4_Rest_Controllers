package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

@Controller
public class Controller1 {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public Controller1(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("/user")
    public String user(@RequestParam("id") Long id, Model model) {
        model.addAttribute("users", userDetailsServiceImpl.findUserById(id));
        return "user";
    }

}
