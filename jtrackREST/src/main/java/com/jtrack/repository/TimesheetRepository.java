package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jtrack.model.Timesheet;

public interface TimesheetRepository extends JpaRepository <Timesheet, String>, JpaSpecificationExecutor<Timesheet> {

	@Modifying
	@Query("update Job j "
			+ "set j.completedHrs = (select sum(completedHrs) from Job where parentJobNo = j.jobNo) "
			+ "where j.jobNo = (select parentJobNo from Job where jobNo = :jobNo)")
	public void refreshCompletedHrsInParentJob(@Param("jobNo") long jobNo);
	
	@Modifying
	@Query("update Job j "
			+ "set j.jobStatus = 'In Progress', "
			+ "j.actualStartDate = (select min(actualStartDate) from Job where parentJobNo = j.jobNo) "
			+ "where j.jobNo = (select parentJobNo from Job where jobNo = :jobNo) "
			+ "and (j.jobStatus = '' or j.jobStatus = null) "
			+ "and j.actualStartDate = null")
	public void refreshStatusInParentJob(@Param("jobNo") long jobNo);
	
	@Modifying
	@Query("update Job j "
			+ "set j.completedHrs = (select sum(workedHrs) from Timesheet where jobNo = j.jobNo) "
			+ "where j.jobNo = :jobNo")
	public void refreshCompletedHrsInJob(@Param("jobNo") long jobNo);
	
	@Modifying
	@Query("update Job j "
			+ "set j.jobStatus = 'In Progress', "
			+ "j.actualStartDate = (select min(workedDate) from Timesheet where jobNo = j.jobNo) "
			+ "where j.jobNo = :jobNo "
			+ "and (j.jobStatus = '' or j.jobStatus = null) "
			+ "and j.actualStartDate = null")
	public void refreshStatusInJob(@Param("jobNo") long jobNo);
}
