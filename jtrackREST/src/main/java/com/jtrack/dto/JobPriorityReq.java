package com.jtrack.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class JobPriorityReq {
	
	@NotNull(message = ValidationUtils.NOTNULL_JOB_PRIORITY_MSG)
	@Pattern(regexp = ValidationUtils.VALID_ID_PATTERN, message = ValidationUtils.INVALID_JOB_PRIORITY_MSG)
	private String jobPriority;
	
    private String jobPriorityDesc;
    private Boolean active;
    
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
	
	@Override
	public String toString() {
		return "JobPriorityReq [jobPriority=" + jobPriority + ", jobPriorityDesc=" + jobPriorityDesc + ", active="
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
		JobPriorityReq other = (JobPriorityReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(jobPriority, other.jobPriority)
				&& Objects.equals(jobPriorityDesc, other.jobPriorityDesc);
	}
}
