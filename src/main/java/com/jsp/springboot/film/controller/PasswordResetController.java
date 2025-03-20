package com.jsp.springboot.film.controller;

import java.util.HashMap;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import com.jsp.springboot.film.serviceimpl.PasswordResetServiceImpl;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:3000"}) // ✅ Ensure frontend can access
public class PasswordResetController {

    @Autowired
    private PasswordResetServiceImpl passwordResetService;

    // ✅ Send reset code
    @PostMapping("/forgot-password")
    public ResponseEntity<Map<String, String>> forgotPassword(@RequestBody Map<String, String> request) {
        Map<String, String> response = new HashMap<>();
        try {
            passwordResetService.sendResetCode(request.get("email"));
            response.put("message", "✅ Reset code sent to your email.");
            return ResponseEntity.ok(response);
        } catch (UsernameNotFoundException e) {
            response.put("error", "❌ Email is not registered.");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        } catch (RuntimeException e) {  // ✅ Handles both RuntimeException & IllegalStateException
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
        } catch (Exception e) {
            response.put("error", "❌ An unknown error occurred.");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }






    // ✅ Verify reset code
    @PostMapping("/verify-reset-code")
    public ResponseEntity<Map<String, String>> verifyResetCode(@RequestBody Map<String, String> request) {
        boolean isValid = passwordResetService.verifyResetCode(request.get("email"), request.get("token"));
        return isValid ? ResponseEntity.ok(Map.of("message", "Valid reset code"))
                : ResponseEntity.badRequest().body(Map.of("message", "Invalid reset code"));
    }

    // ✅ Reset password
    @PostMapping("/reset-password")
    public ResponseEntity<Map<String, String>> resetPassword(@RequestBody Map<String, String> request) {
        passwordResetService.updatePassword(request.get("email"), request.get("newPassword"));
        return ResponseEntity.ok(Map.of("message", "Password updated successfully"));
    }
}
