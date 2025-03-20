package com.jsp.springboot.film.controller;

import com.jsp.springboot.film.dto.PaymentRequest;
import com.jsp.springboot.film.entity.Payment;
import com.jsp.springboot.film.serviceimpl.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    private final PaymentService paymentService;

    @Autowired
    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping("/create-checkout-session")
    public String createCheckoutSession(@RequestBody PaymentRequest request) throws Exception {
        // Create the Stripe Checkout Session
        String sessionUrl = paymentService.createCheckoutSession(request);

        // Save transaction details in the database
        Payment payment = new Payment();
        payment.setUserId(request.getUserId());
        payment.setSessionId(paymentService.getSessionIdFromUrl(sessionUrl)); // Extract session ID from URL
        payment.setCustomerEmail(request.getEmail()); // Assuming email is part of PaymentRequest
        payment.setAmount(request.getAmount());
        payment.setCurrency(request.getCurrency());
        payment.setStatus("PENDING"); // Initial status
        payment.setPaymentDate(LocalDateTime.now()); // Set the payment date
        paymentService.savePayment(payment);

        return sessionUrl;
    }

    @PostMapping("/confirm-payment")
    public String confirmPayment(@RequestParam String sessionId) {
        // Fetch payment details from Stripe (optional)
        // For simplicity, assume the payment is confirmed
        Payment payment = paymentService.findBySessionId(sessionId);
        if (payment != null) {
            payment.setStatus("COMPLETED");
            paymentService.savePayment(payment);
            return "Payment confirmed and saved successfully!";
        } else {
            return "Payment not found!";
        }
    }
}
