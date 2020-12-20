package com.jtrack.dto;

import java.time.LocalDateTime;

public class DeleteResp {

	String message;
	LocalDateTime timestamp = LocalDateTime.now();
	
	public DeleteResp() {
		super();
	}
	
	public DeleteResp(String message) {
		super();
		this.message = message;
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
		return "DeleteResp [message=" + message + ", timestamp=" + timestamp + "]";
	}
}
