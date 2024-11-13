package ru.kata.spring.boot_security.demo.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.kata.spring.boot_security.demo.dto.UserDTO;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.service.UserService;


import java.util.Optional;

@Component
public class UserValidator implements Validator {

    private final UserService userService;

    @Autowired
    public UserValidator(UserService userService) {
        this.userService = userService;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserDTO.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserDTO userToSave = (UserDTO) target;
        Optional<User> user = userService.getUserByEmail(userToSave.getEmail());
        if (user.isPresent()) {
            if (!user.get().getId().equals(userToSave.getId())) {
                errors.rejectValue("email", "", "This email is already taken");
            }
        }
    }
}
