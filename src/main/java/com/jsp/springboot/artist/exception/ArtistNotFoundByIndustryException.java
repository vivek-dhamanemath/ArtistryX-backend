package com.jsp.springboot.artist.exception;

public class ArtistNotFoundByIndustryException extends RuntimeException {

	private String message;

	public ArtistNotFoundByIndustryException(String message) {
		
		this.message = message;

	}

	public String getMessage() {
		return message;
	}
}