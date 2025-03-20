package com.jsp.springboot.artist.utility;

import com.jsp.springboot.film.entity.Artist;

public class ResponseStrcture<T> {

	private int stauscode;
	private String message;
	private T artist;


	public int getStauscode() {
		return stauscode;
	}
	public void setStauscode(int stauscode) {
		this.stauscode = stauscode;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public T getArtist() {
		return artist;
	}
	public void setArtist(T artist) {
		this.artist = artist;
	}

	public void setData(T artistDTOs) {
	}
}
