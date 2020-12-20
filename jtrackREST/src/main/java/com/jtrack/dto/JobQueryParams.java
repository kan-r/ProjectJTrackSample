package com.jtrack.dto;

public class JobQueryParams {
	
	private String jobName = "";
    private String jobType = "";
    private String jobStatus = "";
    private String assignedTo = "";
    private Boolean includeChildJobs = false;
    private String jobNameChild = "";
    private String jobTypeChild = "";
    private String jobStatusChild = "";
    private String assignedToChild = "";
    
	public String getJobName() {
		return jobName;
	}
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
	}
	public Boolean isIncludeChildJobs() {
		return includeChildJobs;
	}
	public void setIncludeChildJobs(Boolean includeChildJobs) {
		this.includeChildJobs = includeChildJobs;
	}
	public String getJobNameChild() {
		return jobNameChild;
	}
	public void setJobNameChild(String jobNameChild) {
		this.jobNameChild = jobNameChild;
	}
	public String getJobTypeChild() {
		return jobTypeChild;
	}
	public void setJobTypeChild(String jobTypeChild) {
		this.jobTypeChild = jobTypeChild;
	}
	public String getJobStatusChild() {
		return jobStatusChild;
	}
	public void setJobStatusChild(String jobStatusChild) {
		this.jobStatusChild = jobStatusChild;
	}
	public String getAssignedToChild() {
		return assignedToChild;
	}
	public void setAssignedToChild(String assignedToChild) {
		this.assignedToChild = assignedToChild;
	}
	
	@Override
	public String toString() {
		return "JobQueryParams [jobName=" + jobName + ", jobType=" + jobType + ", jobStatus=" + jobStatus
				+ ", assignedTo=" + assignedTo + ", includeChildJobs=" + includeChildJobs + ", jobNameChild="
				+ jobNameChild + ", jobTypeChild=" + jobTypeChild + ", jobStatusChild=" + jobStatusChild
				+ ", assignedToChild=" + assignedToChild + "]";
	}
}
