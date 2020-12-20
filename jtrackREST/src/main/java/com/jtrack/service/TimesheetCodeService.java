package com.jtrack.service;

import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.TimesheetCodeReq;
import com.jtrack.dto.TimesheetCodeResp;
import com.jtrack.exception.InvalidDataException;

public interface TimesheetCodeService {

	List<TimesheetCodeResp> getTimesheetCodeList();
	
	TimesheetCodeResp getTimesheetCode(String timesheetCodeId);
	
	TimesheetCodeResp addTimesheetCode(TimesheetCodeReq timesheetCodeReq) throws InvalidDataException;
	
	TimesheetCodeResp updateTimesheetCode(String timesheetCodeId, TimesheetCodeReq timesheetCodeReq) throws InvalidDataException;
	
	DeleteResp deleteTimesheetCode(String timesheetCodeId) throws InvalidDataException;
}
