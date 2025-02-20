package com.jsp.springboot.artist.exception;

public class ArtistNotFoundByIdException extends RuntimeException {

	private String message;

	public ArtistNotFoundByIdException(String message) {
		super();
		this.message = message;

	}

	public String getMessage() {
		return message;
	}

}