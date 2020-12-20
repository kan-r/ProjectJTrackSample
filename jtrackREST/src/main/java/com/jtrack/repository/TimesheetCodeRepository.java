package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrack.model.TimesheetCode;;

public interface TimesheetCodeRepository extends JpaRepository <TimesheetCode, String> {

}
