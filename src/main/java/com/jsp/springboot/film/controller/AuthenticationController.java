package com.jsp.springboot.film.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.jsp.springboot.film.security.JwtUtil;
import com.jsp.springboot.film.service.UserService;
import com.jsp.springboot.film.entity.User;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping({"/api/auth", "https://artistryx.vercel.app/register"})
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
        System.out.println("🔹 Attempting login for: " + user.getUsername());

        User dbUser = userService.findByUsername(user.getUsername())
                .orElseThrow(() -> {
                    System.out.println("❌ User not found: " + user.getUsername());
                    return new UsernameNotFoundException("❌ User not found");
                });

        System.out.println("✅ User found in DB: " + dbUser.getUsername());
        System.out.println("🔹 Entered password: " + user.getPassword());
        System.out.println("🔹 Stored hashed password: " + dbUser.getPassword());

        // 🔥 Compare passwords correctly
        if (!passwordEncoder.matches(user.getPassword(), dbUser.getPassword())) {
            System.out.println("❌ Password does not match!");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Collections.singletonMap("error", "❌ Invalid credentials."));
        }

        System.out.println("✅ Password matched! Generating JWT token...");
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(dbUser.getUsername())
                .password(dbUser.getPassword())
                .roles(dbUser.getRole().replace("ROLE_", ""))
                .build();

        String token = jwtUtil.generateToken(userDetails);
        return ResponseEntity.ok(Map.of("token", token, "role", dbUser.getRole()));
    }




    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User user) {
        try {
            // ✅ Check if email or username already exists
            if (userService.findByUsername(user.getUsername()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                                     .body(Map.of("error", "❌ Username already exists!"));
            }
            if (userService.findByEmail(user.getEmail()).isPresent()) {
                return ResponseEntity.status(HttpStatus.CONFLICT) // 409 Conflict
                                     .body(Map.of("error", "❌ Email already registered!"));
            }

            String message = userService.registerUser(user);
            return ResponseEntity.ok(Map.of("message", message));

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                                 .body(Map.of("error", "❌ Registration failed due to a server error."));
        }
    }
    
    @GetMapping("/check-username")
    public ResponseEntity<Map<String, String>> checkUsername(@RequestParam String username) {
        boolean exists = userService.findByUsername(username).isPresent();
        
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("message", "❌ Username already taken"));
        }
        
        return ResponseEntity.ok(Map.of("message", "✅ Username is available"));
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<Map<String, String>> checkEmailAvailability(@RequestParam String email) {
        boolean exists = userService.findByEmail(email).isPresent();
        if (exists) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("message", "❌ Email already registered!"));
        }
        return ResponseEntity.ok(Map.of("message", "✅ Email is available"));
    }



}
