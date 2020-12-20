package com.jtrack.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="LOGIN_HIST")
public class LoginHist {
	
	@Id 
    @Column(name="HIST_ID", unique=true, nullable=false)
	private String histId;

	@Column(name="USER_ID")
    private String userId;

	@Column(name="IP_ADDR")
    private String ipAddr;
	 
	@Column(name="DATE_CRT")
	private LocalDateTime dateCrt;
	
	
	public LoginHist() {
		super();
	}
	
	public LoginHist(String histId, String userId, String ipAddr, LocalDateTime dateCrt) {
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
	 
	 
}
