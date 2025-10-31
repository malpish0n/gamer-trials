package com.malpishon.gamertrials.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String password;
    private String email;
    private String role;
    private LocalDate birthDate;
    private String avatarUrl;
    private String bio;
    private String location;
    private Integer xp;

    // Getters

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getRole() { return role; }

    public LocalDate getBirthDate() { return birthDate; }

    // Setters

    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setRole(String role) { this.role = role; }

    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }
}
