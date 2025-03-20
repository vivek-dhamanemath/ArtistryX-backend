package com.jsp.springboot.film.controller;

import com.jsp.springboot.film.dto.RegistrationRequest;
import com.jsp.springboot.film.entity.User;
import com.jsp.springboot.film.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class RegistrationController {

    @Autowired
    private UserService userService;

   
}
