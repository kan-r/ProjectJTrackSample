package com.jtrack.dto;

public class JobRespKeyDet {
	
	private Long jobNo;
	private String jobName;
    private String jobDesc;
    private String jobType;
    
    public JobRespKeyDet() {
    	super();
    }
    
	public JobRespKeyDet(long jobNo, String jobName, String jobDesc, String jobType) {
		super();
		this.jobNo = jobNo;
		this.jobName = jobName;
		this.jobDesc = jobDesc;
		this.jobType = jobType;
	}
	
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
		return "JobRespKeyDet [jobNo=" + jobNo + ", jobName=" + jobName + ", jobDesc=" + jobDesc + ", jobType="
				+ jobType + "]";
	}
}
