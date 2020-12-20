package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrack.model.JobStatus;

public interface JobStatusRepository extends JpaRepository <JobStatus, String> {

}
