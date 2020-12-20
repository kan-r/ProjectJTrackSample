package com.jtrack.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class JobResolutionReq {
	
	@NotNull(message = ValidationUtils.NOTNULL_JOB_RESOLUTION_MSG)
	@Pattern(regexp = ValidationUtils.VALID_ID_PATTERN, message = ValidationUtils.INVALID_JOB_RESOLUTION_MSG)
	private String jobResolution;
	
    private String jobResolutionDesc;
    private Boolean active;
    
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
	
	@Override
	public String toString() {
		return "JobResolutionReq [jobResolution=" + jobResolution + ", jobResolutionDesc=" + jobResolutionDesc + ", active="
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
		JobResolutionReq other = (JobResolutionReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(jobResolution, other.jobResolution)
				&& Objects.equals(jobResolutionDesc, other.jobResolutionDesc);
	}
}
