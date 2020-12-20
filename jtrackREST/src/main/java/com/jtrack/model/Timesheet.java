package com.jtrack.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name="TIMESHEET", uniqueConstraints = @UniqueConstraint(columnNames={"USER_ID", "JOB_NO", "WORKED_DATE"}))
public class Timesheet {
	
	@Id 
    @Column(name="TIMESHEET_ID", unique=true, nullable=false)
	private String timesheetId;
	
	@Column(name="USER_ID", nullable=false)
    private String userId;
	
	@Column(name="JOB_NO", nullable=false)
    private Long jobNo;
	
    @Column(name="WORKED_DATE", length=7, nullable=false)
    private LocalDate workedDate;
	
	@Column(name="WORKED_HRS")
    private Double workedHrs;
	
	@Column(name="TIMESHEET_CODE")
    private String timesheetCode;
	
	@Column(name="ACTIVE", precision=1, scale=0)
    private Boolean active;
	
    @Column(name="DATE_CRT")
    private LocalDateTime dateCrt;
	
	@Column(name="USER_CRT")
    private String userCrt;
	
    @Column(name="DATE_MOD")
    private LocalDateTime dateMod;
	
	@Column(name="USER_MOD")
    private String userMod;
    
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "JOB_NO", insertable = false, updatable = false)
    private JobKeyDet jobObj;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "USER_ID", insertable = false, updatable = false)
    private UserKeyDet userObj;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "USER_CRT", insertable = false, updatable = false)
    private UserKeyDet userCrtObj;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "USER_MOD", insertable = false, updatable = false)
    private UserKeyDet userModObj;
	
	
	public Timesheet() {
		super();
	}

	public Timesheet(String timesheetId, String userId, long jobNo, LocalDate workedDate, double workedHrs, String timesheetCode,
			Boolean active, LocalDateTime dateCrt, String userCrt) {
		super();
		this.timesheetId = timesheetId;
		this.userId = userId;
		this.jobNo = jobNo;
		this.workedDate = workedDate;
		this.workedHrs = workedHrs;
		this.timesheetCode = timesheetCode;
		this.active = active;
		this.dateCrt = dateCrt;
		this.userCrt = userCrt;
	}

	public String getTimesheetId() {
		return timesheetId;
	}

	public void setTimesheetId(String timesheetId) {
		this.timesheetId = timesheetId;
	}

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

	public LocalDateTime getDateCrt() {
		return dateCrt;
	}

	public void setDateCrt(LocalDateTime dateCrt) {
		this.dateCrt = dateCrt;
	}

	public String getUserCrt() {
		return userCrt;
	}

	public void setUserCrt(String userCrt) {
		this.userCrt = userCrt;
	}

	public LocalDateTime getDateMod() {
		return dateMod;
	}

	public void setDateMod(LocalDateTime dateMod) {
		this.dateMod = dateMod;
	}

	public String getUserMod() {
		return userMod;
	}

	public void setUserMod(String userMod) {
		this.userMod = userMod;
	}

	public JobKeyDet getJobObj() {
		return jobObj;
	}

	public void setJobObj(JobKeyDet jobObj) {
		this.jobObj = jobObj;
	}

	public UserKeyDet getUserObj() {
		return userObj;
	}

	public void setUserObj(UserKeyDet userObj) {
		this.userObj = userObj;
	}

	public UserKeyDet getUserCrtObj() {
		return userCrtObj;
	}

	public void setUserCrtObj(UserKeyDet userCrtObj) {
		this.userCrtObj = userCrtObj;
	}

	public UserKeyDet getUserModObj() {
		return userModObj;
	}

	public void setUserModObj(UserKeyDet userModObj) {
		this.userModObj = userModObj;
	}

	@Override
	public String toString() {
		return "Timesheet [timesheetId=" + timesheetId + ", userId=" + userId + ", jobNo=" + jobNo + ", workedDate="
				+ workedDate + ", workedHrs=" + workedHrs + ", timesheetCode=" + timesheetCode + ", active=" + active
				+ ", dateCrt=" + dateCrt + ", userCrt=" + userCrt + ", dateMod=" + dateMod + ", userMod=" + userMod
				+ ", jobObj=" + jobObj + ", userObj=" + userObj + ", userCrtObj=" + userCrtObj + ", userModObj="
				+ userModObj + "]";
	}
}
