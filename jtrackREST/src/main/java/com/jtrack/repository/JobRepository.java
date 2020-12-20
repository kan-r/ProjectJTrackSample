package com.jtrack.repository;

import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jtrack.model.Job;

public interface JobRepository extends JpaRepository <Job, String>, JpaSpecificationExecutor<Job> {
	
	public Job findByJobNo(long jobNo);
	public Job findByJobName(String jobName);
	public List<Job> findByJobType(String jobType, Sort sort);
	public List<Job> findByParentJobNo(Long parentJobNo, Sort sort);
	
	@Modifying
	@Query("update Job j "
			+ "set j.estimatedHrs = (select sum(estimatedHrs) from Job where parentJobNo = j.jobNo), "
			+ "j.estimatedStartDate = (select min(estimatedStartDate) from Job where parentJobNo = j.jobNo), "
			+ "j.estimatedEndDate = (select max(estimatedEndDate) from Job where parentJobNo = j.jobNo) "
			+ "where j.jobNo = :jobNo")
	public void refreshParentJob(@Param("jobNo") long jobNo);
	
	@Query("select count(*) from Timesheet where jobNo = :jobNo")
	int getTimesheetCount(@Param("jobNo") long jobNo);

}
