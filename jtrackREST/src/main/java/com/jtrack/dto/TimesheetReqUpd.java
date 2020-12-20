package com.jtrack.dto;

import java.util.Objects;

public class TimesheetReqUpd {
	
    private Double workedHrs;
    private String timesheetCode;
    private Boolean active;
    
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
		return "TimesheetReqUpd [workedHrs=" + workedHrs + ", timesheetCode=" + timesheetCode + ", active=" + active
				+ "]";
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TimesheetReqUpd other = (TimesheetReqUpd) obj;
		return Objects.equals(active, other.active) && Objects.equals(timesheetCode, other.timesheetCode)
				&& Objects.equals(workedHrs, other.workedHrs);
	}
}
