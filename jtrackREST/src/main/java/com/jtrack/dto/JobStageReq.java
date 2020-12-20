package com.jtrack.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class JobStageReq {
	
	@NotNull(message = ValidationUtils.NOTNULL_JOB_STAGE_MSG)
	@Pattern(regexp = ValidationUtils.VALID_ID_PATTERN, message = ValidationUtils.INVALID_JOB_STAGE_MSG)
	private String jobStage;
	
    private String jobStageDesc;
    private Boolean active;
    
	public String getJobStage() {
		return jobStage;
	}
	public void setJobStage(String jobStage) {
		this.jobStage = jobStage;
	}
	public String getJobStageDesc() {
		return jobStageDesc;
	}
	public void setJobStageDesc(String jobStageDesc) {
		this.jobStageDesc = jobStageDesc;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "JobStageReq [jobStage=" + jobStage + ", jobStageDesc=" + jobStageDesc + ", active="
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
		JobStageReq other = (JobStageReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(jobStage, other.jobStage)
				&& Objects.equals(jobStageDesc, other.jobStageDesc);
	}
}
