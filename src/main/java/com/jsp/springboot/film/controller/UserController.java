package com.jsp.springboot.film.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.jsp.springboot.film.security.JwtUtil;
import com.jsp.springboot.film.service.UserService;
import com.jsp.springboot.film.entity.User;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "https://artistryx-a23zdhptw-vivek-s-projects-78825531.vercel.app/login") // Allow frontend access
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    

   
}
