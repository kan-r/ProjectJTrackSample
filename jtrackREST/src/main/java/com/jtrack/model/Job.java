package com.jtrack.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;


@Entity
@Table(name="JOBS", uniqueConstraints = @UniqueConstraint(columnNames={"JOB_NAME"}))
public class Job {
	
	@Id 
    @GeneratedValue(strategy = GenerationType.AUTO, generator = "G1")
    @SequenceGenerator(name = "G1", sequenceName = "JOB_NO_SEQ", allocationSize = 1)
    @Column(name="JOB_NO", unique=true, nullable=false)
	private Long jobNo;
	
	@Column(name="JOB_NAME")
    private String jobName;
	
	@Column(name="JOB_DESC")
    private String jobDesc;
	
	@Column(name="JOB_TYPE")
    private String jobType;
    
    @Column(name="JOB_PRIORITY")
    private String jobPriority;
    
    @Column(name="JOB_STATUS")
    private String jobStatus;
    
    @Column(name="JOB_RESOLUTION")
    private String jobResolution;
    
    @Column(name="JOB_STAGE")
    private String jobStage;
    
    @Column(name="JOB_ORDER")
    private Integer jobOrder;
    
    @Column(name="ASSIGNED_TO")
    private String assignedTo;
    
    @Column(name="TIMESHEET_CODE")
    private String timesheetCode;
    
    @Column(name="ESTIMATED_HRS")
    private Double estimatedHrs;
    
    @Column(name="COMPLETED_HRS")
    private Double completedHrs;
    
    @Column(name="ESTIMATED_START_DATE")
    private LocalDate estimatedStartDate;
    
    @Column(name="ACTUAL_START_DATE")
    private LocalDate actualStartDate;
    
    @Column(name="ESTIMATED_END_DATE")
    private LocalDate estimatedEndDate;
    
    @Column(name="ACTUAL_END_DATE")
    private LocalDate actualEndDate;
    
    @Column(name="PARENT_JOB_NO")
    private Long parentJobNo;
    
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
    
    @Column(name="JOB_REF")
    private String jobRef;
    
    @ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name = "PARENT_JOB_NO", insertable = false, updatable = false)
    private JobKeyDet parentJobObj;
    
    @ManyToOne(fetch=FetchType.EAGER, optional=true)
    @JoinColumn(name = "ASSIGNED_TO", insertable = false, updatable = false)
    private UserKeyDet assignedToObj;
    
    @ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "USER_CRT", insertable = false, updatable = false)
    private UserKeyDet userCrtObj;
	
	@ManyToOne(fetch=FetchType.EAGER, optional=true)
	@JoinColumn(name = "USER_MOD", insertable = false, updatable = false)
    private UserKeyDet userModObj;
	
	
	public Job() {
		super();
	}
	
	public Job(String jobName, String jobDesc, String jobType, Long parentJobNo, Boolean active, LocalDateTime dateCrt,
			String userCrt) {
		super();
		this.jobName = jobName;
		this.jobDesc = jobDesc;
		this.jobType = jobType;
		this.parentJobNo = parentJobNo;
		this.active = active;
		this.dateCrt = dateCrt;
		this.userCrt = userCrt;
	}
	
	
	public Long getJobNo() {
		return jobNo;
	}
	public void setJobNo(Long jobNo) {
		this.jobNo = jobNo;
	}
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
	public String getJobType() {
		return jobType;
	}
	public void setJobType(String jobType) {
		this.jobType = jobType;
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
	public String getJobStage() {
		return jobStage;
	}
	public void setJobStage(String jobStage) {
		this.jobStage = jobStage;
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
	public Double getCompletedHrs() {
		return completedHrs;
	}
	public void setCompletedHrs(Double completedHrs) {
		this.completedHrs = completedHrs;
	}
	public LocalDate getEstimatedStartDate() {
		return estimatedStartDate;
	}
	public void setEstimatedStartDate(LocalDate estimatedStartDate) {
		this.estimatedStartDate = estimatedStartDate;
	}
	public LocalDate getActualStartDate() {
		return actualStartDate;
	}
	public void setActualStartDate(LocalDate actualStartDate) {
		this.actualStartDate = actualStartDate;
	}
	public LocalDate getEstimatedEndDate() {
		return estimatedEndDate;
	}
	public void setEstimatedEndDate(LocalDate estimatedEndDate) {
		this.estimatedEndDate = estimatedEndDate;
	}
	public LocalDate getActualEndDate() {
		return actualEndDate;
	}
	public void setActualEndDate(LocalDate actualEndDate) {
		this.actualEndDate = actualEndDate;
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
	public String getJobRef() {
		return jobRef;
	}
	public void setJobRef(String jobRef) {
		this.jobRef = jobRef;
	}
	public JobKeyDet getParentJobObj() {
		return parentJobObj;
	}
	public void setParentJobObj(JobKeyDet parentJobObj) {
		this.parentJobObj = parentJobObj;
	}
	public UserKeyDet getAssignedToObj() {
		return assignedToObj;
	}
	public void setAssignedToObj(UserKeyDet assignedToObj) {
		this.assignedToObj = assignedToObj;
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
		return "Job [jobNo=" + jobNo + ", jobName=" + jobName + ", jobDesc=" + jobDesc + ", jobType=" + jobType
				+ ", jobPriority=" + jobPriority + ", jobStatus=" + jobStatus + ", jobResolution=" + jobResolution
				+ ", jobStage=" + jobStage + ", jobOrder=" + jobOrder + ", assignedTo=" + assignedTo
				+ ", timesheetCode=" + timesheetCode + ", estimatedHrs=" + estimatedHrs + ", completedHrs="
				+ completedHrs + ", estimatedStartDate=" + estimatedStartDate + ", actualStartDate=" + actualStartDate
				+ ", estimatedEndDate=" + estimatedEndDate + ", actualEndDate=" + actualEndDate + ", parentJobNo="
				+ parentJobNo + ", active=" + active + ", dateCrt=" + dateCrt + ", userCrt=" + userCrt + ", dateMod="
				+ dateMod + ", userMod=" + userMod + ", jobRef=" + jobRef + "]";
	}
}
