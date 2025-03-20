package com.jsp.springboot.film.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.jsp.springboot.film.security.JwtUtil;
import com.jsp.springboot.film.service.UserService;
import com.jsp.springboot.film.entity.User;

import java.util.Collections;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
//CrossOrigin(origins = "http://localhost:3000") // Allow requests from frontend
@CrossOrigin(origins = "http://localhost:3000", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT, RequestMethod.DELETE})

public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthenticationController(AuthenticationManager authenticationManager, UserService userService, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/login")
    public ResponseEntity<Map<String, String>> loginUser(@RequestBody User user) {
        System.out.println("Attempting login for: " + user.getUsername());

        User dbUser = userService.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    System.out.println("User not found: " + user.getUsername());
                    return new UsernameNotFoundException("User not found");
                });

        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            System.out.println("Password does not match!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "Invalid credentials."));
        }

        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(dbUser.getUsername())
                .password(dbUser.getPassword())
                .roles(dbUser.getRole().replace("ROLE_", ""))
                .build();

        String token = jwtUtil.generateToken(userDetails);

        // Return token, role, and username in response
        return ResponseEntity.ok(Map.of(
                "token", token,
                "role", dbUser.getRole(),
                "username", dbUser.getUsername()  // Include the username here
        ));
    }


    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Username already exists!"));
            }
            if (userService.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT)
                        .body(Map.of("error", "Email already registered!"));
            }

            String message = userService.registerUser(user);
            return ResponseEntity.ok(Map.of("message", message));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Registration failed due to a server error."));
        }
    }

    @GetMapping("/check-username/{username}")
    public ResponseEntity<Map<String, Boolean>> checkUsername(@PathVariable String username) {
        boolean exists = userService.findByUsername(username).isPresent();
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }

    @GetMapping("/check-email/{email}")
    public ResponseEntity<Map<String, Boolean>> checkEmailAvailability(@PathVariable String email) {
        boolean exists = userService.findByEmail(email).isPresent();
        return ResponseEntity.ok(Collections.singletonMap("exists", exists));
    }
}