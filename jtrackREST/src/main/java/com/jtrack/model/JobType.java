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
@Table(name="JOB_TYPE")
public class JobType {
	
	@Id 
    @Column(name="JOB_TYPE", unique=true, nullable=false)
	private String jobType;
	
	@Column(name="JOB_TYPE_DESC")
    private String jobTypeDesc;
	
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
	
	
	public JobType() {
		super();
	}
    
	public JobType(String jobType, String jobTypeDesc, Boolean active, LocalDateTime dateCrt, String userCrt,
			LocalDateTime dateMod, String userMod) {
		super();
		this.jobType = jobType;
		this.jobTypeDesc = jobTypeDesc;
		this.active = active;
		this.dateCrt = dateCrt;
		this.userCrt = userCrt;
		this.dateMod = dateMod;
		this.userMod = userMod;
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
		return "JobType [jobType=" + jobType + ", jobTypeDesc=" + jobTypeDesc + ", active=" + active + ", dateCrt="
				+ dateCrt + ", userCrt=" + userCrt + ", dateMod=" + dateMod + ", userMod=" + userMod + "]";
	}
}
