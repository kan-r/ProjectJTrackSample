package com.jtrack.dto;

import java.time.LocalDateTime;

public class JobStatusResp {

	private String jobStatus;
    private String jobStatusDesc;
    private Boolean active;
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public JobStatusResp() {
    	super();
    }
    
	public JobStatusResp(String jobStatus, String jobStatusDesc, Boolean active) {
		super();
		this.jobStatus = jobStatus;
		this.jobStatusDesc = jobStatusDesc;
		this.active = active;
	}
	
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getJobStatusDesc() {
		return jobStatusDesc;
	}
	public void setJobStatusDesc(String jobStatusDesc) {
		this.jobStatusDesc = jobStatusDesc;
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
		return "JobStatusResp [jobStatus=" + jobStatus + ", jobStatusDesc=" + jobStatusDesc + ", active=" + active
				+ ", dateCrt=" + dateCrt + ", userCrtObj=" + userCrtObj + ", dateMod=" + dateMod + ", userModObj="
				+ userModObj + "]";
	}
}
