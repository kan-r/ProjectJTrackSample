package com.jtrack.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class UserKeyDet {
	
	 @Id 
	 @Column(name="USER_ID")
	 private String userId;
	 
	 @Column(name="FIRST_NAME")
     private String firstName;
	 
	 @Column(name="LAST_NAME")
     private String lastName;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
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

	@Override
	public String toString() {
		return "UserKeyDet [userId=" + userId + ", firstName=" + firstName + ", lastName=" + lastName + "]";
	}
}
