package com.voiceinventory.backend.controller;

import com.voiceinventory.backend.entity.User;
import com.voiceinventory.backend.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5176"})
public class UserController {

    private final UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {

        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Email already registered.");
        }

        User savedUser = userRepository.save(user);

        return ResponseEntity.ok(savedUser);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody User loginRequest) {

        String username = loginRequest.getName();
        String password = loginRequest.getPassword();

        Optional<User> userOptional =
                userRepository.findByEmail(username);

        if (userOptional.isEmpty()) {
            userOptional = userRepository.findByNameIgnoreCase(username);
        }

        if (userOptional.isEmpty()) {
            return ResponseEntity.status(401)
                    .body("Incorrect username/email or password.");
        }

        User user = userOptional.get();

        if (!user.getPassword().equals(password)) {
            return ResponseEntity.status(401)
                    .body("Incorrect username/email or password.");
        }

        return ResponseEntity.ok(user);
    }
}
