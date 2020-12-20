package com.jtrack.dto;

import java.time.LocalDate;
import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class JobReq {

	@NotNull(message = ValidationUtils.NOTNULL_JOB_MSG)
	@Pattern(regexp = ValidationUtils.VALID_ID_PATTERN, message = ValidationUtils.INVALID_JOB_MSG)
	private String jobName;
	
    private String jobDesc;
    private String jobRef;
    private String jobType;
    private String jobStage;
    private String jobPriority;
    private String jobStatus;
    private String jobResolution;
    private Integer jobOrder;
    private String assignedTo;
    private String timesheetCode;
    private Double estimatedHrs;
    private LocalDate estimatedStartDate;
    private LocalDate estimatedEndDate;
    private Long parentJobNo;
	private Boolean active;	
	
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
	public String getAssignedTo() {
		return assignedTo;
	}
	public void setAssignedTo(String assignedTo) {
		this.assignedTo = assignedTo;
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
	public LocalDate getEstimatedStartDate() {
		return estimatedStartDate;
	}
	public void setEstimatedStartDate(LocalDate estimatedStartDate) {
		this.estimatedStartDate = estimatedStartDate;
	}
	public LocalDate getEstimatedEndDate() {
		return estimatedEndDate;
	}
	public void setEstimatedEndDate(LocalDate estimatedEndDate) {
		this.estimatedEndDate = estimatedEndDate;
	}
	public Long getParentJobNo() {
		return parentJobNo;
	}
	public void setParentJobNo(Long parentJobNo) {
		this.parentJobNo = parentJobNo;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "JobReq [jobName=" + jobName + ", jobDesc=" + jobDesc + ", jobRef=" + jobRef + ", jobType=" + jobType
				+ ", jobStage=" + jobStage + ", jobPriority=" + jobPriority + ", jobStatus=" + jobStatus
				+ ", jobResolution=" + jobResolution + ", jobOrder=" + jobOrder + ", assignedTo=" + assignedTo
				+ ", timesheetCode=" + timesheetCode + ", estimatedHrs=" + estimatedHrs + ", estimatedStartDate="
				+ estimatedStartDate + ", estimatedEndDate=" + estimatedEndDate + ", parentJobNo=" + parentJobNo
				+ ", active=" + active + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobReq other = (JobReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(assignedTo, other.assignedTo)
				&& Objects.equals(estimatedEndDate, other.estimatedEndDate)
				&& Objects.equals(estimatedHrs, other.estimatedHrs)
				&& Objects.equals(estimatedStartDate, other.estimatedStartDate)
				&& Objects.equals(jobDesc, other.jobDesc) && Objects.equals(jobName, other.jobName)
				&& Objects.equals(jobOrder, other.jobOrder) && Objects.equals(jobPriority, other.jobPriority)
				&& Objects.equals(jobRef, other.jobRef) && Objects.equals(jobResolution, other.jobResolution)
				&& Objects.equals(jobStage, other.jobStage) && Objects.equals(jobStatus, other.jobStatus)
				&& Objects.equals(jobType, other.jobType) && Objects.equals(parentJobNo, other.parentJobNo)
				&& Objects.equals(timesheetCode, other.timesheetCode);
	}
}
