package com.malpishon.gamertrials.model;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class User {

    public User() {
        this.xp = 0;
        this.level = 1;
        this.showBirthDate = false;
    }

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
    private Integer level;
    @Transient
    private boolean showBirthDate;
    @Transient
    private Integer xpToNextLevel;
    @Transient
    private Integer age;


    // Getters

    public Long getId() { return id; }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getRole() { return role; }

    public LocalDate getBirthDate() { return birthDate; }

    public String getAvatarUrl() { return avatarUrl; }

    public String getBio() { return bio; }

    public String getLocation() { return location; }

    public Integer getXp() { return xp; }

    public Integer getLevel() { return level; }

    public boolean isShowBirthDate() { return showBirthDate; }

    // Setters

    public void setId(Long id) { this.id = id; }

    public void setUsername(String username) { this.username = username; }

    public void setPassword(String password) { this.password = password; }

    public void setEmail(String email) { this.email = email; }

    public void setRole(String role) { this.role = role; }

    public void setBirthDate(LocalDate birthDate) { this.birthDate = birthDate; }

    public void setAvatarUrl(String avatarUrl) { this.avatarUrl = avatarUrl; }

    public void setBio(String bio) { this.bio = bio; }

    public void setLocation(String location) { this.location = location; }

    public void setXp(Integer xp) { this.xp = xp; }

    public void setLevel(Integer level) { this.level = level; }

    public void setShowBirthDate(boolean showBirthDate) { this.showBirthDate = showBirthDate; }
}
