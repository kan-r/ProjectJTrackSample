package com.jtrack.dto;

import java.time.LocalDateTime;

public class ErrorResp {

	String error;
	String message;
	LocalDateTime timestamp = LocalDateTime.now();
	
	public String getError() {
		return error;
	}
	public void setError(String error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public LocalDateTime getTimestamp() {
		return timestamp;
	}
	public void setTimestamp(LocalDateTime timestamp) {
		this.timestamp = timestamp;
	}
	
	@Override
	public String toString() {
		return "ErrorResp [error=" + error + ", message=" + message + ", timestamp=" + timestamp + "]";
	}
}
