package com.jsp.springboot.artist.exception;

public class ArtistNotFoundByNameException extends RuntimeException {

	private String message;

	public ArtistNotFoundByNameException(String message) {
		super();
		this.message = message;

	}

	public String getMessage() {
		return message;
	}

}