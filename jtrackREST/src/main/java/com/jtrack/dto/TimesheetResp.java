package com.jtrack.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class TimesheetResp {

	private String timesheetId;
	
    private String userId;
    private Long jobNo;
    private LocalDate workedDate;
	
    private Double workedHrs;
    private String timesheetCode;
    private Boolean active;
    
    private UserRespKeyDet userObj;
    private JobRespKeyDet jobObj;
    
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public TimesheetResp() {
    	super();
    }
    
	public TimesheetResp(String timesheetId, String userId, long jobNo, LocalDate workedDate, double workedHrs,
			String timesheetCode, Boolean active) {
		super();
		this.timesheetId = timesheetId;
		this.userId = userId;
		this.jobNo = jobNo;
		this.workedDate = workedDate;
		this.workedHrs = workedHrs;
		this.timesheetCode = timesheetCode;
		this.active = active;
	}
	
	public String getTimesheetId() {
		return timesheetId;
	}
	public void setTimesheetId(String timesheetId) {
		this.timesheetId = timesheetId;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getJobNo() {
		return jobNo;
	}
	public void setJobNo(Long jobNo) {
		this.jobNo = jobNo;
	}
	public LocalDate getWorkedDate() {
		return workedDate;
	}
	public void setWorkedDate(LocalDate workedDate) {
		this.workedDate = workedDate;
	}
	public Double getWorkedHrs() {
		return workedHrs;
	}
	public void setWorkedHrs(Double workedHrs) {
		this.workedHrs = workedHrs;
	}
	public String getTimesheetCode() {
		return timesheetCode;
	}
	public void setTimesheetCode(String timesheetCode) {
		this.timesheetCode = timesheetCode;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	public UserRespKeyDet getUserObj() {
		return userObj;
	}
	public void setUserObj(UserRespKeyDet userObj) {
		this.userObj = userObj;
	}
	public JobRespKeyDet getJobObj() {
		return jobObj;
	}
	public void setJobObj(JobRespKeyDet jobObj) {
		this.jobObj = jobObj;
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
		return "TimesheetResp [timesheetId=" + timesheetId + ", userId=" + userId + ", jobNo=" + jobNo + ", workedDate="
				+ workedDate + ", workedHrs=" + workedHrs + ", timesheetCode=" + timesheetCode + ", active=" + active
				+ ", userObj=" + userObj + ", jobObj=" + jobObj + ", dateCrt=" + dateCrt + ", userCrtObj=" + userCrtObj
				+ ", dateMod=" + dateMod + ", userModObj=" + userModObj + "]";
	}
}
