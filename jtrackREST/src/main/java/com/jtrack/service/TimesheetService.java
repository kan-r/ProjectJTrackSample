package com.jtrack.service;

import java.time.LocalDate;
import java.util.List;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.TimesheetReq;
import com.jtrack.dto.TimesheetReqUpd;
import com.jtrack.dto.TimesheetResp;
import com.jtrack.exception.InvalidDataException;

public interface TimesheetService {

	List<TimesheetResp> getTimesheetList();
	
	List<TimesheetResp> getTimesheetList(String userId, LocalDate workedDateFrom, LocalDate workedDateTo) throws InvalidDataException;
	
	TimesheetResp getTimesheet(String timesheetId);
	
	TimesheetResp addTimesheet(TimesheetReq timesheetReq) throws InvalidDataException;
	
	TimesheetResp updateTimesheet(String timesheetId, TimesheetReqUpd timesheetReqUpd) throws InvalidDataException;
	
	DeleteResp deleteTimesheet(String timesheetId) throws InvalidDataException;
}
