package com.jtrack.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class JobTypeReq {
	
	@NotNull(message = ValidationUtils.NOTNULL_JOB_TYPE_MSG)
	@Pattern(regexp = ValidationUtils.VALID_ID_PATTERN, message = ValidationUtils.INVALID_JOB_TYPE_MSG)
	private String jobType;
	
    private String jobTypeDesc;
    private Boolean active;
    
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
	
	@Override
	public String toString() {
		return "JobTypeReq [jobType=" + jobType + ", jobTypeDesc=" + jobTypeDesc + ", active="
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
		JobTypeReq other = (JobTypeReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(jobType, other.jobType)
				&& Objects.equals(jobTypeDesc, other.jobTypeDesc);
	}
}
