package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrack.model.JobStage;;

public interface JobStageRepository extends JpaRepository <JobStage, String> {

}
