package com.jtrack.dto;

import java.time.LocalDateTime;

public class JobTypeResp {

	private String jobType;
    private String jobTypeDesc;
    private Boolean active;
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public JobTypeResp() {
    	super();
    }
    
	public JobTypeResp(String jobType, String jobTypeDesc, Boolean active) {
		super();
		this.jobType = jobType;
		this.jobTypeDesc = jobTypeDesc;
		this.active = active;
	}
	
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJobTypeDesc() {
		return jobTypeDesc;
	}
	public void setJobTypeDesc(String jobTypeDesc) {
		this.jobTypeDesc = jobTypeDesc;
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
		return "JobTypeResp [jobType=" + jobType + ", jobTypeDesc=" + jobTypeDesc + ", active=" + active + ", dateCrt="
				+ dateCrt + ", userCrtObj=" + userCrtObj + ", dateMod=" + dateMod + ", userModObj=" + userModObj + "]";
	}
}
