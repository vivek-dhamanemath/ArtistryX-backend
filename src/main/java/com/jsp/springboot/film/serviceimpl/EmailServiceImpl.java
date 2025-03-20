package com.jsp.springboot.film.serviceimpl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailServiceImpl {
    @Autowired
    private JavaMailSender mailSender;

    public void sendResetEmail(String to, String token) {
        System.out.println("📧 Sending reset code: " + token + " to " + to);  // Debugging
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject("Password Reset Code");
        message.setText("Your password reset code is: " + token + "\nThis code expires in 10 minutes.");
        mailSender.send(message);
        System.out.println("✅ Email sent successfully to " + to);
    }

}

