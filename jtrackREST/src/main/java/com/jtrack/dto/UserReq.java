package com.jtrack.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class UserReq {
	
	@NotNull(message = ValidationUtils.NOTNULL_USER_MSG)
	@Pattern(regexp = ValidationUtils.VALID_USER_PATTERN, message = ValidationUtils.INVALID_USER_MSG)
	private String userId;
	
	@NotNull(message = ValidationUtils.NOTNULL_PASSWORD_MSG)
	@Pattern(regexp = ValidationUtils.VALID_PASSWORD_PATTERN, message = ValidationUtils.INVALID_PASSWORD_MSG)
	private String pword;
	
	private String firstName;
	private String lastName;
	private Boolean active;
	
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
	
	@Override
	public String toString() {
		return "UserReq [userId=" + userId + ", pword=" + pword + ", firstName=" + firstName + ", lastName=" + lastName
				+ ", active=" + active + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserReq other = (UserReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(firstName, other.firstName)
				&& Objects.equals(lastName, other.lastName) && Objects.equals(pword, other.pword)
				&& Objects.equals(userId, other.userId);
	}
}
