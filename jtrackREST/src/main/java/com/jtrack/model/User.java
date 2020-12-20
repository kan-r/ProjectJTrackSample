package com.jtrack.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class User {

	 @Id 
	 @Column(name="USER_ID", unique=true, nullable=false)
	 private String userId;
	 
	 @Column(name="PWORD")
     private String pword;
	 
	 @Column(name="FIRST_NAME")
     private String firstName;
	 
	 @Column(name="LAST_NAME")
     private String lastName;
	 
	 @Column(name="ACTIVE", precision=1, scale=0)
     private Boolean active;
	 
	 @Column(name="DATE_CRT")
     private LocalDateTime dateCrt;
	 
	 @Column(name="USER_CRT")
     private String userCrt;
	 
	 @Column(name="DATE_MOD")
     private LocalDateTime dateMod;
	 
	 @Column(name="USER_MOD")
     private String userMod;
	 
	 @OneToOne(fetch=FetchType.EAGER, optional=true)
	 @JoinColumn(name = "USER_CRT", insertable = false, updatable = false)
	 private UserKeyDet userCrtObj;
		
	 @OneToOne(fetch=FetchType.EAGER, optional=true)
	 @JoinColumn(name = "USER_MOD", insertable = false, updatable = false)
	 private UserKeyDet userModObj;
	 
	 
	public User() {
		super();
	}

	public User(String userId, String pword, String firstName, String lastName, Boolean active, LocalDateTime dateCrt,
			String userCrt, LocalDateTime dateMod, String userMod) {
		super();
		this.userId = userId;
		this.pword = pword;
		this.firstName = firstName;
		this.lastName = lastName;
		this.active = active;
		this.dateCrt = dateCrt;
		this.userCrt = userCrt;
		this.dateMod = dateMod;
		this.userMod = userMod;
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

	public String getUserCrt() {
		return userCrt;
	}

	public void setUserCrt(String userCrt) {
		this.userCrt = userCrt;
	}

	public LocalDateTime getDateMod() {
		return dateMod;
	}

	public void setDateMod(LocalDateTime dateMod) {
		this.dateMod = dateMod;
	}

	public String getUserMod() {
		return userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

	public UserKeyDet getUserCrtObj() {
		return userCrtObj;
	}

	public void setUserCrtObj(UserKeyDet userCrtObj) {
		this.userCrtObj = userCrtObj;
	}

	public UserKeyDet getUserModObj() {
		return userModObj;
	}

	public void setUserModObj(UserKeyDet userModObj) {
		this.userModObj = userModObj;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", pword=" + pword + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", active=" + active + ", dateCrt=" + dateCrt + ", userCrt=" + userCrt + ", dateMod=" + dateMod
				+ ", userMod=" + userMod + ", userCrtObj=" + userCrtObj + ", userModObj=" + userModObj + "]";
	}
}
