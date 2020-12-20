package com.jtrack.dto;

import java.time.LocalDateTime;

public class UserResp {

	private String userId;
	private String pword;
	private String firstName;
	private String lastName;
	private Boolean active;
	private LocalDateTime dateCrt;
	private UserRespKeyDet userCrtObj;
	private LocalDateTime dateMod;
	private UserRespKeyDet userModObj;
	
	public UserResp() {
		super();
	}
	
	public UserResp(String userId, String pword, String firstName, String lastName, Boolean active) {
		super();
		this.userId = userId;
		this.pword = pword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getPword() {
		return pword;
	}
	public void setPword(String pword) {
		this.pword = pword;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public LocalDateTime getDateCrt() {
		return dateCrt;
	}
	public void setDateCrt(LocalDateTime dateCrt) {
		this.dateCrt = dateCrt;
	}
	public UserRespKeyDet getUserCrtObj() {
		return userCrtObj;
	}
	public void setUserCrtObj(UserRespKeyDet userCrtObj) {
		this.userCrtObj = userCrtObj;
	}
	public LocalDateTime getDateMod() {
		return dateMod;
	}
	public void setDateMod(LocalDateTime dateMod) {
		this.dateMod = dateMod;
	}
	public UserRespKeyDet getUserModObj() {
		return userModObj;
	}
	public void setUserModObj(UserRespKeyDet userModObj) {
		this.userModObj = userModObj;
	}
	
	@Override
	public String toString() {
		return "UserResp [userId=" + userId + ", pword=" + pword + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", active=" + active + ", dateCrt=" + dateCrt + ", userCrtObj=" + userCrtObj + ", dateMod=" + dateMod
				+ ", userModObj=" + userModObj + "]";
	}
}
