package ru.kata.spring.boot_security.demo.controller;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.repository.RoleRepo;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api")
public class MainRestController {
    private final UserService userService;
    private final RoleRepo roleRepo;
    private final ModelMapper modelMapper;

    @Autowired
    public MainRestController(UserService service, ModelMapper modelMapper, RoleRepo roleRepo) {
        this.userService = service;
        this.modelMapper = modelMapper;
        this.roleRepo = roleRepo;
    }

    private UserDTO convertToUserDTO(User user) {
        return modelMapper.map(user, UserDTO.class);
    }

    private User convertToUser(UserDTO userDTO) {
        return modelMapper.map(userDTO, User.class);
    }

    @GetMapping("/user")
    public ResponseEntity<UserDTO> userPage(Principal principal) {
        User user = userService.getUserByFirstname(principal.getName());
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(convertToUserDTO(user));
        }
    }

    @GetMapping("/admin")
    public ResponseEntity<List<User>> getUserList() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/admin/{id}")
    public ResponseEntity<UserDTO> getUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return ResponseEntity.ok(convertToUserDTO(user));
        }
    }

    @GetMapping("/roles")
    public ResponseEntity<List<Role>> allRoles() {
        return ResponseEntity.ok(roleRepo.findAll());
    }

    @PostMapping("/admin")
    public ResponseEntity<HttpStatus> saveUser(@RequestBody User userDTO) {
        userService.saveUser(null, userDTO);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @PatchMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody User user, @PathVariable("id") Long id) {
        userService.saveUser(userService.getUserById(id), user);
        return ResponseEntity.ok(HttpStatus.OK);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<HttpStatus> deleteUser(@PathVariable Long id) {
        User user = userService.getUserById(id);
        if (user == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        userService.deleteUser(user);
        return ResponseEntity.ok(HttpStatus.OK);
    }
}
