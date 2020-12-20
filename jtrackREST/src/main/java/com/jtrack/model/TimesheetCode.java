package com.jtrack.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="TIMESHEET_CODE")
public class TimesheetCode {
	
	@Id 
    @Column(name="TIMESHEET_CODE", unique=true, nullable=false)
	private String timesheetCode;
	
	@Column(name="TIMESHEET_CODE_DESC")
    private String timesheetCodeDesc;
	
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
    
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "USER_CRT", insertable = false, updatable = false)
    private UserKeyDet userCrtObj;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "USER_MOD", insertable = false, updatable = false)
    private UserKeyDet userModObj;
	
	
	public TimesheetCode() {
		super();
	}
    
	public TimesheetCode(String timesheetCode, String timesheetCodeDesc, Boolean active, LocalDateTime dateCrt,
			String userCrt, LocalDateTime dateMod, String userMod) {
		super();
		this.timesheetCode = timesheetCode;
		this.timesheetCodeDesc = timesheetCodeDesc;
		this.active = active;
		this.dateCrt = dateCrt;
		this.userCrt = userCrt;
		this.dateMod = dateMod;
		this.userMod = userMod;
	}
	
	
	public String getTimesheetCode() {
		return timesheetCode;
	}
	public void setTimesheetCode(String timesheetCode) {
		this.timesheetCode = timesheetCode;
	}
	public String getTimesheetCodeDesc() {
		return timesheetCodeDesc;
	}
	public void setTimesheetCodeDesc(String timesheetCodeDesc) {
		this.timesheetCodeDesc = timesheetCodeDesc;
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
		return "TimesheetCode [timesheetCode=" + timesheetCode + ", timesheetCodeDesc=" + timesheetCodeDesc
				+ ", active=" + active + ", dateCrt=" + dateCrt + ", userCrt=" + userCrt + ", dateMod=" + dateMod
				+ ", userMod=" + userMod + "]";
	}
}
