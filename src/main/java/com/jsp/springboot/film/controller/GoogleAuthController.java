package com.jsp.springboot.film.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.jsp.springboot.film.entity.User;
import com.jsp.springboot.film.repositroy.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
@RestController
@RequestMapping("api/auth")
public class GoogleAuthController {

    private static final String CLIENT_ID = "979081682205-24a4jjvat3enner3bvnvf4rmua8cs0rs.apps.googleusercontent.com";

    @Value("${jwt.secret}") // ✅ Inject secret key from properties
    private String secretKey;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/google-login")
    public ResponseEntity<?> googleLogin(@RequestBody Map<String, String> request) {
        String token = request.get("credential");

        if (token == null || token.trim().isEmpty()) {
            System.err.println("Received empty or null Google token.");
            return ResponseEntity.badRequest().body("Invalid Google Token: No token provided");
        }

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(
                    new NetHttpTransport(), new GsonFactory())
                    .setAudience(Collections.singletonList(CLIENT_ID))
                    .build();

            GoogleIdToken idToken = verifier.verify(token);
            if (idToken != null) {
                GoogleIdToken.Payload payload = idToken.getPayload();

                String email = payload.getEmail();
                String name = (String) payload.get("name");
                String picture = (String) payload.get("picture");

                Optional<User> existingUser = userRepository.findByEmail(email);
                User user = existingUser.orElseGet(() -> {
                    User newUser = new User();
                    newUser.setEmail(email);
                    newUser.setUsername(name);
                    newUser.setPassword("");
                    newUser.setRole("ROLE_USER");
                    return userRepository.save(newUser);
                });

                String jwtToken = generateJwtToken(user);

                return ResponseEntity.ok(Map.of(
                        "email", email,
                        "name", name,
                        "picture", picture,
                        "role", user.getRole(),
                        "token", jwtToken
                ));
            } else {
                System.err.println("Invalid Google Token: Verification failed.");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid Google Token");
            }
        } catch (Exception e) {
            System.err.println("Error during Google login: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Google Authentication Failed");
        }
    }

    private String generateJwtToken(User user) {
        return Jwts.builder()
                .setSubject(user.getEmail())
                .claim("role", user.getRole())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 3600000))
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }
}
