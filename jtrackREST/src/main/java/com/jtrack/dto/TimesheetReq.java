package com.jtrack.dto;

import java.time.LocalDate;
import java.util.Objects;

import com.jtrack.validation.ValidationUtils;
import com.jtrack.validation.TimesheetNotBlank;

@TimesheetNotBlank(message = ValidationUtils.NOTBLANK_TIMESHEET_MSG)
public class TimesheetReq {

    private String userId;
    private Long jobNo;
    private LocalDate workedDate;
	
    private Double workedHrs;
    private String timesheetCode;
    private Boolean active;
    
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Long getJobNo() {
		return jobNo;
	}
	public void setJobNo(Long jobNo) {
		this.jobNo = jobNo;
	}
	public void setJobNo(long jobNo) {
		this.jobNo = jobNo;
	}
	public LocalDate getWorkedDate() {
		return workedDate;
	}
	public void setWorkedDate(LocalDate workedDate) {
		this.workedDate = workedDate;
	}
	public Double getWorkedHrs() {
		return workedHrs;
	}
	public void setWorkedHrs(Double workedHrs) {
		this.workedHrs = workedHrs;
	}
	public String getTimesheetCode() {
		return timesheetCode;
	}
	public void setTimesheetCode(String timesheetCode) {
		this.timesheetCode = timesheetCode;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "TimesheetReq [userId=" + userId + ", jobNo=" + jobNo + ", workedDate=" + workedDate + ", workedHrs="
				+ workedHrs + ", timesheetCode=" + timesheetCode + ", active=" + active + "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimesheetReq other = (TimesheetReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(jobNo, other.jobNo)
				&& Objects.equals(timesheetCode, other.timesheetCode) && Objects.equals(userId, other.userId)
				&& Objects.equals(workedDate, other.workedDate) && Objects.equals(workedHrs, other.workedHrs);
	}
}
