package com.jtrack.dto;

import java.util.Objects;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

import com.jtrack.validation.ValidationUtils;

public class TimesheetCodeReq {
	
	@NotNull(message = ValidationUtils.NOTNULL_TIMESHEET_CODE_MSG)
	@Pattern(regexp = ValidationUtils.VALID_ID_PATTERN, message = ValidationUtils.INVALID_TIMESHEET_CODE_MSG)
	private String timesheetCode;
	
    private String timesheetCodeDesc;
    private Boolean active;
    
	public String getTimesheetCode() {
		return timesheetCode;
	}
	public void setTimesheetCode(String timesheetCode) {
		this.timesheetCode = timesheetCode;
	}
	public String getTimesheetCodeDesc() {
		return timesheetCodeDesc;
	}
	public void setTimesheetCodeDesc(String timesheetCodeDesc) {
		this.timesheetCodeDesc = timesheetCodeDesc;
	}
	public Boolean getActive() {
		return active;
	}
	public void setActive(Boolean active) {
		this.active = active;
	}
	
	@Override
	public String toString() {
		return "TimesheetCodeReq [timesheetCode=" + timesheetCode + ", timesheetCodeDesc=" + timesheetCodeDesc + ", active="
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
		TimesheetCodeReq other = (TimesheetCodeReq) obj;
		return Objects.equals(active, other.active) && Objects.equals(timesheetCode, other.timesheetCode)
				&& Objects.equals(timesheetCodeDesc, other.timesheetCodeDesc);
	}
}
