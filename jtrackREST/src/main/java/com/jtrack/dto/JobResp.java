package com.jtrack.dto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class JobResp {
	
	private Long jobNo;
	private String jobName;
    private String jobDesc;
    private String jobRef;
    private String jobType;
    private String jobStage;
    private String jobPriority;
    private String jobStatus;
    private String jobResolution;
    private Integer jobOrder;
    private UserRespKeyDet assignedToObj;
    private String timesheetCode;
    private Double estimatedHrs;
    private Double completedHrs;
    private LocalDate estimatedStartDate;
    private LocalDate actualStartDate;
    private LocalDate estimatedEndDate;
    private LocalDate actualEndDate;
    private JobRespKeyDet parentJobObj;
	private Boolean active;
    private LocalDateTime dateCrt;
    private UserRespKeyDet userCrtObj;
    private LocalDateTime dateMod;
    private UserRespKeyDet userModObj;
    
    public JobResp() {
    	super();
    }
    
	public JobResp(long jobNo, String jobName, String jobDesc, String jobType, JobRespKeyDet parentJobObj,
			Boolean active) {
		super();
		this.jobNo = jobNo;
		this.jobName = jobName;
		this.jobDesc = jobDesc;
		this.jobType = jobType;
		this.parentJobObj = parentJobObj;
		this.active = active;
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
	public String getJobRef() {
		return jobRef;
	}
	public void setJobRef(String jobRef) {
		this.jobRef = jobRef;
	}
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
	}
	public String getJobStage() {
		return jobStage;
	}
	public void setJobStage(String jobStage) {
		this.jobStage = jobStage;
	}
	public String getJobPriority() {
		return jobPriority;
	}
	public void setJobPriority(String jobPriority) {
		this.jobPriority = jobPriority;
	}
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getJobResolution() {
		return jobResolution;
	}
	public void setJobResolution(String jobResolution) {
		this.jobResolution = jobResolution;
	}
	public Integer getJobOrder() {
		return jobOrder;
	}
	public void setJobOrder(Integer jobOrder) {
		this.jobOrder = jobOrder;
	}
	public UserRespKeyDet getAssignedToObj() {
		return assignedToObj;
	}
	public void setAssignedToObj(UserRespKeyDet assignedToObj) {
		this.assignedToObj = assignedToObj;
	}
	public String getTimesheetCode() {
		return timesheetCode;
	}
	public void setTimesheetCode(String timesheetCode) {
		this.timesheetCode = timesheetCode;
	}
	public Double getEstimatedHrs() {
		return estimatedHrs;
	}
	public void setEstimatedHrs(Double estimatedHrs) {
		this.estimatedHrs = estimatedHrs;
	}
	public Double getCompletedHrs() {
		return completedHrs;
	}
	public void setCompletedHrs(Double completedHrs) {
		this.completedHrs = completedHrs;
	}
	public LocalDate getEstimatedStartDate() {
		return estimatedStartDate;
	}
	public void setEstimatedStartDate(LocalDate estimatedStartDate) {
		this.estimatedStartDate = estimatedStartDate;
	}
	public LocalDate getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(LocalDate actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public LocalDate getEstimatedEndDate() {
		return estimatedEndDate;
	}
	public void setEstimatedEndDate(LocalDate estimatedEndDate) {
		this.estimatedEndDate = estimatedEndDate;
	}
	public LocalDate getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(LocalDate actualEndDate) {
		this.actualEndDate = actualEndDate;
	}
	public JobRespKeyDet getParentJobObj() {
		return parentJobObj;
	}
	public void setParentJobObj(JobRespKeyDet parentJobObj) {
		this.parentJobObj = parentJobObj;
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
		return "JobResp [jobNo=" + jobNo + ", jobName=" + jobName + ", jobDesc=" + jobDesc + ", jobRef=" + jobRef
				+ ", jobType=" + jobType + ", jobStage=" + jobStage + ", jobPriority=" + jobPriority + ", jobStatus="
				+ jobStatus + ", jobResolution=" + jobResolution + ", jobOrder=" + jobOrder + ", assignedToObj="
				+ assignedToObj + ", timesheetCode=" + timesheetCode + ", estimatedHrs=" + estimatedHrs
				+ ", completedHrs=" + completedHrs + ", estimatedStartDate=" + estimatedStartDate + ", actualStartDate="
				+ actualStartDate + ", estimatedEndDate=" + estimatedEndDate + ", actualEndDate=" + actualEndDate
				+ ", parentJobObj=" + parentJobObj + ", active=" + active + ", dateCrt=" + dateCrt + ", userCrtObj="
				+ userCrtObj + ", dateMod=" + dateMod + ", userModObj=" + userModObj + "]";
	}
}
