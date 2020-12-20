package com.jtrack.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class JobStatusReq {
	
	@NotNull(message = ValidationUtils.NOTNULL_JOB_STATUS_MSG)
	@Pattern(regexp = ValidationUtils.VALID_ID_PATTERN, message = ValidationUtils.INVALID_JOB_STATUS_MSG)
	private String jobStatus;
	
    private String jobStatusDesc;
    private Boolean active;
    
	public String getJobStatus() {
		return jobStatus;
	}
	public void setJobStatus(String jobStatus) {
		this.jobStatus = jobStatus;
	}
	public String getJobStatusDesc() {
		return jobStatusDesc;
	}
	public void setJobStatusDesc(String jobStatusDesc) {
		this.jobStatusDesc = jobStatusDesc;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "JobStatusReq [jobStatus=" + jobStatus + ", jobStatusDesc=" + jobStatusDesc + ", active="
				+ active + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		JobStatusReq other = (JobStatusReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(jobStatus, other.jobStatus)
				&& Objects.equals(jobStatusDesc, other.jobStatusDesc);
	}
}
