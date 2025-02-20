package com.jsp.springboot.film;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

import com.jsp.springboot.film.controller.ArtistController;
import com.jsp.springboot.film.entity.Artist;

@SpringBootApplication
public class SpringBootFilmApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBootFilmApplication.class, args);


	}

}
