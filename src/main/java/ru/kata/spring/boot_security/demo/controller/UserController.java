package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.security.UserDetailsImpl;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;

import java.util.stream.Collectors;

@RestController
public class UserController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;

    @Autowired
    public UserController(UserDetailsServiceImpl userDetailsServiceImpl) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
    }

    @GetMapping("/user")
    public String user(Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            userDetails = (UserDetailsImpl) authentication.getPrincipal();
            User user = userDetailsServiceImpl.findUserById(((UserDetailsImpl) authentication.
                    getPrincipal()).getUser().getId());
            model.addAttribute("user", user);
            Boolean hasRole = user.getAuthorities().stream().anyMatch(g -> g.getAuthority().equals("ROLE_ADMIN"));
            model.addAttribute("hasRole", hasRole);
        }
        model.addAttribute("username", userDetails.getUser().getUsername());
        String roles = userDetails.getUser().getRoles()
                .stream()
                .map(role -> role.getName().replace("ROLE_", "")) // Убираем ROLE_
                .collect(Collectors.joining(" ")); // Объединяем с пробелами
        model.addAttribute("roles", roles);

        return "user";
    }
}
