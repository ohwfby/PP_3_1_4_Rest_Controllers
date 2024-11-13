package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.security.Principal;

@Controller
public class UserControllerDemo {

    @Autowired
    private UserService userService;

    @GetMapping("/user")
    public String user(Principal principal, Model model) {
        User user = userService.getUserByFirstname(principal.getName());
        model.addAttribute("user", user);
        Boolean hasRole = user.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
        model.addAttribute("hasRole", hasRole);
        return "user";
    }
}
