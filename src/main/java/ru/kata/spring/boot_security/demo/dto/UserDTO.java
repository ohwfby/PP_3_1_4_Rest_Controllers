package ru.kata.spring.boot_security.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

import java.util.ArrayList;
import java.util.List;

public class UserDTO {

//    @JsonProperty
    private Long id;

//    @JsonProperty
    @NotEmpty(message = "First name should not be empty")
    @Size(max = 32, message = "First name should be shorter than 32 characters")
    private String firstName;

//    @JsonProperty
    @NotEmpty(message = "Last name should not be empty")
    @Size(max = 32, message = "Last name should be shorter than 32 characters")
    private String lastName;

//    @JsonProperty
    @NotEmpty(message = "Email should not be empty")
    @Email(message = "Email should be valid")
    private String email;

//    @JsonProperty
    @Min(value = 1, message = "Age should be greater than 0")
    private int age;

//    @JsonProperty
    @NotEmpty(message = "Password should not be empty")
    private String userPassword;

//    @JsonProperty
    private List<RoleDTO> roles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getUserPassword() {
        return userPassword;
    }

    public void setUserPassword(String userPassword) {
        this.userPassword = userPassword;
    }

    public List<RoleDTO> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDTO> roles) {
        this.roles = roles;
    }
}