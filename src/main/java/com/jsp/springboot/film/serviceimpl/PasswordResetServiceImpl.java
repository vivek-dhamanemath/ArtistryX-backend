package com.jsp.springboot.film.serviceimpl;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UsernameNotFoundException; 
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.jsp.springboot.film.entity.ResetToken;
import com.jsp.springboot.film.entity.User;
import com.jsp.springboot.film.repositroy.ResetTokenRepository;
import com.jsp.springboot.film.repositroy.UserRepository;

import jakarta.transaction.Transactional;

@Service
public class PasswordResetServiceImpl {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Autowired
    private EmailServiceImpl emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void sendResetCode(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        
        if (user.isEmpty()) {
            System.out.println("❌ User not found: " + email);
            throw new UsernameNotFoundException("User not found");
        }

        Optional<ResetToken> existingToken = resetTokenRepository.findByEmail(email);

        // ✅ If a valid reset token already exists, prevent generating a new one
        if (existingToken.isPresent()) {
            ResetToken token = existingToken.get();
            if (token.getExpiryTime().isAfter(LocalDateTime.now())) {
                System.out.println("⚠️ Reset token already sent. Not generating a new one.");
                throw new IllegalStateException("⚠️ Reset code already sent to your email. Please check or wait 10 minutes.");
            } else {
                System.out.println("🔄 Expired token found. Deleting and generating a new one.");
                resetTokenRepository.deleteByEmail(email);
            }
        }


        // ✅ Generate a new token only if no valid token exists
        String token = UUID.randomUUID().toString().substring(0, 6);
        System.out.println("✅ Generated Token: " + token);

        ResetToken resetToken = new ResetToken();
        resetToken.setEmail(email);
        resetToken.setToken(token);
        resetToken.setExpiryTime(LocalDateTime.now().plusMinutes(10));

        resetTokenRepository.save(resetToken);
        emailService.sendResetEmail(email, token);
        System.out.println("✅ Reset Code Sent to: " + email);
    }




    public boolean verifyResetCode(String email, String token) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByEmail(email);
        
        if (resetToken.isPresent()) {
            if (resetToken.get().getExpiryTime().isBefore(LocalDateTime.now())) {
                System.out.println("❌ Reset code expired for: " + email);
                return false;
            }
            return resetToken.get().getToken().equals(token);
        }

        System.out.println("❌ Invalid reset token for: " + email);
        return false;
    }


    @Transactional  // ✅ Fix: Ensure delete operation is inside a transaction
    public void updatePassword(String email, String newPassword) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isEmpty()) throw new UsernameNotFoundException("User not found");

        user.get().setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user.get());

        resetTokenRepository.deleteByEmail(email);  // ✅ Fix: Now inside a transaction

        System.out.println("✅ Password updated successfully for: " + email);
    }
}


