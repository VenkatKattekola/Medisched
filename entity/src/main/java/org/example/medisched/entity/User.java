package org.example.medisched.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

@Entity
@Table(name = "Users") // Ensure this matches your table name exactly (case-sensitive if your DB is)
public class User {
    public enum Role {
        PATIENT,
        DOCTOR,
        ADMIN
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "userid") // Using lowercase based on your working code
    private Long userId;

    @NotBlank(message = "Username cannot be empty")
    @Size(max = 50, message = "Username must be less than or equal to 50 characters")
    @Column(name = "username", unique = true, nullable = false) // Using lowercase
    private String username;

    @NotBlank(message = "Password hash cannot be empty")
    @Size(max = 255, message = "Password hash must be less than or equal to 255 characters")
    @Column(name = "passwordhash", nullable = false) // Using lowercase
    private String passwordHash;

    @NotBlank(message = "Full name cannot be empty")
    @Size(max = 100, message = "Full name must be less than or equal to 100 characters")
    @Column(name = "fullname", nullable = false) // Using lowercase
    private String fullName;

    @Size(max = 15, message = "Cell number must be less than or equal to 15 characters")
    @Column(name = "cell", length = 15) // Using lowercase
    private String cell;

    @NotBlank(message = "Email cannot be empty")
    @Size(max = 100, message = "Email must be less than or equal to 100 characters")
    @Email(message = "Email must be a valid email format")
    @Column(name = "email", unique = true, nullable = false) // Using lowercase
    private String email;

    @Enumerated(EnumType.STRING) // Stores the enum as a String in the database
    @Column(name = "role", nullable = false) // Using lowercase
    private Role role;

    @Column(name = "createdat", nullable = false, updatable = false) // Using lowercase, Hibernate manages
    private LocalDateTime createdAt;

    @Column(name = "updatedat") // Using lowercase, Hibernate manages
    private LocalDateTime updatedAt;

    // --- Constructors ---
    public User() {}

    public User(String username, String passwordHash, String fullName, String cell, String email, Role role) {
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.cell = cell;
        this.email = email;
        this.role = role;
    }

    // --- JPA Lifecycle Callbacks for automatic timestamping ---
    // These methods are called automatically by JPA before persisting or updating
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // --- Getters & setters ---
    // Make sure these match the field names (e.g., getUserId() for userId)
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }
    public void setUsername(String username) {
        this.username = username;
    }

    public String getPasswordHash() {
        return passwordHash;
    }
    public void setPasswordHash(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    public String getFullName() {
        return fullName;
    }
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getCell() {
        return cell;
    }
    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getEmail() {
        return email;
    }
    public void setEmail(String email) {
        this.email = email;
    }

    public Role getRole() {
        return role;
    }
    public void setRole(Role role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}