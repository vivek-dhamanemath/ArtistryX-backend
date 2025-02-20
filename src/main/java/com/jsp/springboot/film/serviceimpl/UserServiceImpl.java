package com.jsp.springboot.film.serviceimpl;

import com.jsp.springboot.film.entity.User;
import com.jsp.springboot.film.repositroy.UserRepository;
import com.jsp.springboot.film.service.UserService;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // ✅ Implement findByUsername()
    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public String registerUser(User user) {
        if (userRepository.findByUsername(user.getUsername()).isPresent()) {
            logger.error("❌ Username '{}' already exists!", user.getUsername());
            throw new IllegalStateException("❌ Username already exists!");
        }
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            logger.error("❌ Email '{}' is already registered!", user.getEmail());
            throw new IllegalStateException("❌ Email already registered!");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);

        logger.info("✅ User '{}' registered successfully!", user.getUsername());
        return "✅ User registered successfully!";
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with email: " + email));

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(), user.getPassword(), AuthorityUtils.createAuthorityList(user.getRole())
        );
    }

    @Override
    public String getUserRole(String username) {
        return userRepository.findByUsername(username)
                .map(User::getRole)
                .orElse("ROLE_USER");  // Default to ROLE_USER if user not found
    }
}
