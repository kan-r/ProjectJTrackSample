package com.jtrack.dto;

import java.time.LocalDateTime;

public class JobStageResp {

	private String jobStage;
    private String jobStageDesc;
    private Boolean active;
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public JobStageResp() {
    	super();
    }
    
	public JobStageResp(String jobStage, String jobStageDesc, Boolean active) {
		super();
		this.jobStage = jobStage;
		this.jobStageDesc = jobStageDesc;
		this.active = active;
	}
	
	public String getJobStage() {
		return jobStage;
	}
	public void setJobStage(String jobStage) {
		this.jobStage = jobStage;
	}
	public String getJobStageDesc() {
		return jobStageDesc;
	}
	public void setJobStageDesc(String jobStageDesc) {
		this.jobStageDesc = jobStageDesc;
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
		return "JobStageResp [jobStage=" + jobStage + ", jobStageDesc=" + jobStageDesc + ", active=" + active
				+ ", dateCrt=" + dateCrt + ", userCrtObj=" + userCrtObj + ", dateMod=" + dateMod + ", userModObj="
				+ userModObj + "]";
	}
}
