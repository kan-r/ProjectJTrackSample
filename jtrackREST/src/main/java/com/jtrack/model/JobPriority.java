package com.jtrack.model;

import java.time.LocalDateTime;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="JOB_PRIORITY")
public class JobPriority {

	 @Id 
	 @Column(name="JOB_PRIORITY", unique=true, nullable=false)
	 private String jobPriority;
	 
	 @Column(name="JOB_PRIORITY_DESC")
     private String jobPriorityDesc;
	 
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
     
	
	public JobPriority() {
		 super();
	}
	 
	public JobPriority(String jobPriority, String jobPriorityDesc, Boolean active, LocalDateTime dateCrt,
			String userCrt, LocalDateTime dateMod, String userMod) {
		super();
		this.jobPriority = jobPriority;
		this.jobPriorityDesc = jobPriorityDesc;
		this.active = active;
		this.dateCrt = dateCrt;
		this.userCrt = userCrt;
		this.dateMod = dateMod;
		this.userMod = userMod;
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
		return "JobPriority [jobPriority=" + jobPriority + ", jobPriorityDesc=" + jobPriorityDesc + ", active=" + active
				+ ", userCrt=" + userCrt + ", userMod=" + userMod + "]";
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobPriority other = (JobPriority) obj;
		return Objects.equals(active, other.active) && Objects.equals(jobPriority, other.jobPriority)
				&& Objects.equals(jobPriorityDesc, other.jobPriorityDesc);
	}
}
