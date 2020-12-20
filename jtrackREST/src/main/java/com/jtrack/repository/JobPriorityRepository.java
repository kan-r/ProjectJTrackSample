package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrack.model.JobPriority;;

public interface JobPriorityRepository extends JpaRepository <JobPriority, String> {

}
