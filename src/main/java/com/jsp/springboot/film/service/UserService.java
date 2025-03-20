package com.jsp.springboot.film.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.jsp.springboot.film.entity.User;

public interface UserService extends UserDetailsService {
    String registerUser(User user);

	String getUserRole(String username);
	
	Optional<User> findByUsername(String username); // 🔴 ADD THIS METHOD

	Optional<User> findByEmail(String email);
}
