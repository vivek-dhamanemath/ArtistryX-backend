package com.jsp.springboot.film.serviceimpl;

import com.jsp.springboot.film.dto.PaymentRequest;
import com.jsp.springboot.film.entity.Payment;

import com.jsp.springboot.film.repositroy.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public PaymentService(PaymentRepository paymentRepository) {
        this.paymentRepository = paymentRepository;
    }

    public void saveTransaction(String userId, String sessionId, String customerEmail, Long amount, String currency, String status) {
        Payment payment = new Payment();
        payment.setUserId(userId);
        payment.setSessionId(sessionId);
        payment.setCustomerEmail(customerEmail);
        payment.setAmount(amount);
        payment.setCurrency(currency);
        payment.setStatus(status);
        payment.setPaymentDate(LocalDateTime.now());
        paymentRepository.save(payment);
    }

    public String createCheckoutSession(PaymentRequest request) {
        // Implement the logic to create a Stripe Checkout Session
        // For now, returning a dummy URL
        return "https://checkout.stripe.com/session/cs_test_12345";
    }

    public String getSessionIdFromUrl(String sessionUrl) {
        // Extract the session ID from the URL
        // Assuming the URL format is "https://checkout.stripe.com/session/cs_test_12345"
        String[] parts = sessionUrl.split("/");
        return parts[parts.length - 1];
    }

    public void savePayment(Payment payment) {
        paymentRepository.save(payment);
    }

    public Payment findBySessionId(String sessionId) {
        Optional<Payment> paymentOptional = paymentRepository.findBySessionId(sessionId);
        return paymentOptional.orElse(null);
    }
}
