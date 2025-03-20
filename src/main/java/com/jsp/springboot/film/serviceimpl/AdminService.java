package com.jsp.springboot.film.serviceimpl;

import com.jsp.springboot.film.entity.User;
import com.jsp.springboot.film.repositroy.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {

    @Autowired
    private UserRepository userRepository;

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
}