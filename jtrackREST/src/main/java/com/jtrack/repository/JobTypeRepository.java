package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrack.model.JobType;;

public interface JobTypeRepository extends JpaRepository <JobType, String> {

}
