package com.jsp.springboot.artist.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.jsp.springboot.artist.exception.ArtistNotFoundByIdException;
import com.jsp.springboot.artist.exception.ArtistNotFoundByIndustryException;
import com.jsp.springboot.artist.exception.ArtistNotFoundByNameException;

@RestControllerAdvice	
public class ApplicationHandler {

	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> artistNotFoundById(ArtistNotFoundByIdException ex){
		
		ErrorStructure<String> errorStructure = new ErrorStructure<>();
		errorStructure.setErrorcode(HttpStatus.NOT_FOUND.value());
		errorStructure.setErrorMessage(ex.getMessage());
		errorStructure.setError("Artist with the required id is not avaliable in the database");
		
		return new ResponseEntity<ErrorStructure<String>>(errorStructure, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> artistNotFoundByName(ArtistNotFoundByNameException ex){
		
		ErrorStructure<String> errorStructure = new ErrorStructure<>();
		errorStructure.setErrorcode(HttpStatus.NOT_FOUND.value());
		errorStructure.setErrorMessage(ex.getMessage());
		errorStructure.setError("Artist with the required name is not avaliable in the database");
		
		return new ResponseEntity<ErrorStructure<String>>(errorStructure, HttpStatus.NOT_FOUND);
		
	}
	
	@ExceptionHandler
	public ResponseEntity<ErrorStructure<String>> artistNotFoundByIndustry(ArtistNotFoundByIndustryException ex){
		
		ErrorStructure<String> errorStructure = new ErrorStructure<>();
		errorStructure.setErrorcode(HttpStatus.NOT_FOUND.value());
		errorStructure.setErrorMessage(ex.getMessage());
		errorStructure.setError("Artist with the required industry is not avaliable in the database");
		
		return new ResponseEntity<ErrorStructure<String>>(errorStructure, HttpStatus.NOT_FOUND);
		
	}
	
	
}
