package org.example.medisched.service.security.services;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.medisched.entity.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    private Long id; // Corresponds to userId in your User entity
    private String username;
    private String email;

    @JsonIgnore // Important: Do not serialize password to JSON response
    private String password;

    private Collection<? extends GrantedAuthority> authorities;

    // Constructor to build UserDetailsImpl from your User entity
    public UserDetailsImpl(Long id, String username, String email, String password,
                           Collection<? extends GrantedAuthority> authorities) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.authorities = authorities;
    }

    // Static factory method to build UserDetailsImpl from a User entity
    public static UserDetailsImpl build(User user) {
        List<GrantedAuthority> authorities = user.getRole().name().equals("Patient")
                ? List.of(new SimpleGrantedAuthority("ROLE_PATIENT")) // Map "Patient" enum to "ROLE_PATIENT" Spring Security role
                : List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name().toUpperCase())); // Handle other roles

        return new UserDetailsImpl(
                user.getUserId(),
                user.getUsername(),
                user.getEmail(),
                user.getPasswordHash(), // Get the hashed password
                authorities);
    }


    // --- NEW / VERIFIED GETTERS REQUIRED BY AuthController ---

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    // --- METHODS REQUIRED BY UserDetails Interface ---

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // You can implement account expiration logic if needed
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // You can implement account locking logic if needed
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // You can implement credential expiration logic if needed
    }

    @Override
    public boolean isEnabled() {
        return true; // You can implement account enabled/disabled logic if needed
    }

    // --- Equals and HashCode for comparison ---
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}