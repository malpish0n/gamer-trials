package com.malpishon.gamertrials.service;

import com.malpishon.gamertrials.model.User;
import com.malpishon.gamertrials.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(User user) {
        if (user.getEmail() != null) {
            user.setEmail(user.getEmail().trim().toLowerCase());
        }
        if (user.getUsername() != null) {
            user.setUsername(user.getUsername().trim());
        }

        if (user.getXp() == null) user.setXp(0);
        if (user.getLevel() == null) user.setLevel(1);

        if (user.getEmail() != null && userRepository.existsByEmailIgnoreCase(user.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        if (user.getUsername() != null && userRepository.existsByUsernameIgnoreCase(user.getUsername())) {
            throw new RuntimeException("Username already exists");
        }

        user.setRole("USER");

        String encodedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
}
