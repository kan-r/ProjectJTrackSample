package com.jtrack.dto;

import java.time.LocalDateTime;

public class TimesheetCodeResp {

	private String timesheetCode;
    private String timesheetCodeDesc;
    private Boolean active;
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public TimesheetCodeResp() {
    	super();
    }
    
	public TimesheetCodeResp(String timesheetCode, String timesheetCodeDesc, Boolean active) {
		super();
		this.timesheetCode = timesheetCode;
		this.timesheetCodeDesc = timesheetCodeDesc;
		this.active = active;
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
		return "TimesheetCodeResp [timesheetCode=" + timesheetCode + ", timesheetCodeDesc=" + timesheetCodeDesc
				+ ", active=" + active + ", dateCrt=" + dateCrt + ", userCrtObj=" + userCrtObj + ", dateMod=" + dateMod
				+ ", userModObj=" + userModObj + "]";
	}
}
