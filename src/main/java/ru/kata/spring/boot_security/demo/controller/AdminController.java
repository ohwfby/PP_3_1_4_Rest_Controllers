package ru.kata.spring.boot_security.demo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import ru.kata.spring.boot_security.demo.entity.Role;
import ru.kata.spring.boot_security.demo.entity.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepository;
import ru.kata.spring.boot_security.demo.service.RegistrationService;
import ru.kata.spring.boot_security.demo.service.UserDetailsServiceImpl;
import ru.kata.spring.boot_security.demo.util.UserValidator;
import javax.validation.Valid;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final UserValidator userValidator;
    private final RegistrationService registrationService;

    @Autowired
    public AdminController(UserDetailsServiceImpl userDetailsServiceImpl, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, UserValidator userValidator,
                           RegistrationService registrationService) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.userValidator = userValidator;
        this.registrationService = registrationService;
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String admin(Model model) {
        model.addAttribute("users", userDetailsServiceImpl.allUsers());
        return "admin";
    }

    @GetMapping("/edit")
    public String editPage(@RequestParam("id") Long id, Model model) {
        model.addAttribute("user", userDetailsServiceImpl.findUserById(id));
        List<Role> roles = roleRepository.findAll();
        model.addAttribute("roles", roles);
        return "/admin/edit";
    }

    @GetMapping("/add")
    public String showAddUserPage(@ModelAttribute("user") User user) {
        return "admin/add";
    }

    @PostMapping("/add")
    public String add( @ModelAttribute("user") @Valid User user,
                       BindingResult bindingResult) {
        userValidator.validate(user, bindingResult);

        if (bindingResult.hasErrors()) {
            return "admin/add";
        }
        registrationService.register(user);
        return "redirect:/admin";
    }

    @PostMapping("/edit")
    public String edit(@Valid @ModelAttribute("user") User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            return "/admin/edit";
        }
        User existingUser = userDetailsServiceImpl.findUserById(user.getId());
        existingUser.setUsername(user.getUsername());
        existingUser.setYearOfBirth(user.getYearOfBirth());
        existingUser.setPassword(passwordEncoder.encode(user.getPassword()));
        Set<Role> roles = new HashSet<>();

        for (Role role : user.getRoles()) {
            String roleName = role.getName();

            Role dbRole = roleRepository.findByName(roleName);
            roles.add(dbRole);
        }
        existingUser.setRoles(roles);

        userDetailsServiceImpl.save(existingUser);
        return "redirect:/admin";
    }

    @RequestMapping("/delete")
    public String delete(@RequestParam Long id) {
        userDetailsServiceImpl.delete(id);
        return "redirect:/admin";
    }
}
