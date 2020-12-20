package com.jtrack.dto;

import java.time.LocalDateTime;

public class JobResolutionResp {

	private String jobResolution;
    private String jobResolutionDesc;
    private Boolean active;
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public JobResolutionResp() {
    	super();
    }
    
	public JobResolutionResp(String jobResolution, String jobResolutionDesc, Boolean active) {
		super();
		this.jobResolution = jobResolution;
		this.jobResolutionDesc = jobResolutionDesc;
		this.active = active;
	}
	
	public String getJobResolution() {
		return jobResolution;
	}
	public void setJobResolution(String jobResolution) {
		this.jobResolution = jobResolution;
	}
	public String getJobResolutionDesc() {
		return jobResolutionDesc;
	}
	public void setJobResolutionDesc(String jobResolutionDesc) {
		this.jobResolutionDesc = jobResolutionDesc;
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
		return "JobResolutionResp [jobResolution=" + jobResolution + ", jobResolutionDesc=" + jobResolutionDesc
				+ ", active=" + active + ", dateCrt=" + dateCrt + ", userCrtObj=" + userCrtObj + ", dateMod=" + dateMod
				+ ", userModObj=" + userModObj + "]";
	}
}
