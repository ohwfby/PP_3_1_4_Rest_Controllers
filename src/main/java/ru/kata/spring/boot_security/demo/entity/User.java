package ru.kata.spring.boot_security.demo.entity;

import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, unique = true)
    @NotEmpty(message = "Name should not be empty")
    @NotBlank(message = "Name should not be blank")
    @Size(min = 2, max = 30, message = "Name should be between 2 and 30 characters")
    private String username;

    @Column(name = "password")
    @NotEmpty(message = "Password should not be empty")
    @NotBlank(message = "Password should not be blank")
    @Size(min = 2, max = 68, message = "Password should be between 2 and 30 characters")
    private String password;

    @Min(value = 1900, message = "Year of birth must be over 1900")
    @Column(name = "year_of_birth")
    private Integer yearOfBirth;
    @Column(name = "roles")
    @ManyToMany(fetch = FetchType.LAZY)
    private Set<Role> roles;

    public User() {
    }

    public User(String username, String password, Integer yearOfBirth, Set<Role> roles) {
        this.username = username;
        this.password = password;
        this.yearOfBirth = yearOfBirth;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public Collection<Role> getAuthorities() {
        return roles;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = this.yearOfBirth;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", yearOfBirth=" + yearOfBirth + '}';
    }

    public Set<Role> getRoles() {
        return roles;
    }

    public void setRoles(Set<Role> roles) {
            this.roles = roles;
    }

    public String rolesToString() {
        return roles.stream().map(c -> c.toString().replaceAll("ROLE_|\\\\|[|\\\\]|", "")).collect(Collectors.joining(", "));
    }
}
