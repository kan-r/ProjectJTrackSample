package com.jtrack.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="JOBS")
public class JobKeyDet {
	
	@Id
	@Column(name="JOB_NO")
	private Long jobNo;
	
	@Column(name="JOB_NAME")
	private String jobName;
	
	@Column(name="JOB_DESC")
    private String jobDesc;
	
	@Column(name="JOB_TYPE")
    private String jobType;
	
	public Long getJobNo() {
		return jobNo;
	}
	public void setJobNo(Long jobNo) {
		this.jobNo = jobNo;
	}
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobDesc() {
		return jobDesc;
	}
	public void setJobDesc(String jobDesc) {
		this.jobDesc = jobDesc;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	
	@Override
	public String toString() {
		return "JobKeyDet [jobNo=" + jobNo + ", jobName=" + jobName + ", jobDesc=" + jobDesc + ", jobType=" + jobType
				+ "]";
	}
}
