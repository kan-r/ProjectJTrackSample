package com.jtrack.dto;

import java.time.LocalDateTime;

public class JobPriorityResp {

	private String jobPriority;
    private String jobPriorityDesc;
    private Boolean active;
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public JobPriorityResp() {
    	super();
    }
    
	public JobPriorityResp(String jobPriority, String jobPriorityDesc, Boolean active) {
		super();
		this.jobPriority = jobPriority;
		this.jobPriorityDesc = jobPriorityDesc;
		this.active = active;
	}
	
	public String getJobPriority() {
		return jobPriority;
	}
	public void setJobPriority(String jobPriority) {
		this.jobPriority = jobPriority;
	}
	public String getJobPriorityDesc() {
		return jobPriorityDesc;
	}
	public void setJobPriorityDesc(String jobPriorityDesc) {
		this.jobPriorityDesc = jobPriorityDesc;
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
		return "JobPriorityResp [jobPriority=" + jobPriority + ", jobPriorityDesc=" + jobPriorityDesc + ", active="
				+ active + ", dateCrt=" + dateCrt + ", userCrtObj=" + userCrtObj + ", dateMod=" + dateMod
				+ ", userModObj=" + userModObj + "]";
	}
}
