package com.jtrack.dto;

import java.time.LocalDateTime;

public class LoginHistResp {

	private String histId;
    private String userId;
    private String ipAddr;
	private LocalDateTime dateCrt;
	
	public LoginHistResp() {
		super();
	}
	
	public LoginHistResp(String histId, String userId, String ipAddr, LocalDateTime dateCrt) {
		super();
		this.histId = histId;
		this.userId = userId;
		this.ipAddr = ipAddr;
		this.dateCrt = dateCrt;
	}

	public String getHistId() {
		return histId;
	}

	public void setHistId(String histId) {
		this.histId = histId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getIpAddr() {
		return ipAddr;
	}

	public void setIpAddr(String ipAddr) {
		this.ipAddr = ipAddr;
	}

	public LocalDateTime getDateCrt() {
		return dateCrt;
	}

	public void setDateCrt(LocalDateTime dateCrt) {
		this.dateCrt = dateCrt;
	}

	@Override
	public String toString() {
		return "LoginHistResp [histId=" + histId + ", userId=" + userId + ", ipAddr=" + ipAddr + ", dateCrt=" + dateCrt
				+ "]";
	}
}
