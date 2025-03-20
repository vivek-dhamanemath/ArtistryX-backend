package com.jsp.springboot.artist.utility;

public class ErrorStructure<T> {

	private int errorcode;
	private String errorMessage;
	private T error;
	
	
	public int getErrorcode() {
		return errorcode;
	}
	public void setErrorcode(int errorcode) {
		this.errorcode = errorcode;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public T getError() {
		return error;
	}
	public void setError(T error) {
		this.error = error;
	}
	
	
}
