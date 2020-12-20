package com.jtrack.controller;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.TimesheetReq;
import com.jtrack.dto.TimesheetReqUpd;
import com.jtrack.dto.TimesheetResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.TimesheetService;
import com.jtrack.util.GenUtils;

@RestController
@RequestMapping("/timesheets")
public class TimesheetController {

	@Autowired
	TimesheetService timesheetService;
	
	@GetMapping("")
	public List<TimesheetResp> getTimesheetList(){
		return timesheetService.getTimesheetList();
	}
	
	@GetMapping(path = "", params = {"userId", "workedDateFrom", "workedDateTo"})
	public List<TimesheetResp> getTimesheetListByParams(@RequestParam Map<String,String> params) throws InvalidDataException{
		
		String userId = params.get("userId"); 
		LocalDate workedDateFrom = GenUtils.toDate(params.get("workedDateFrom")); 
		LocalDate workedDateTo = GenUtils.toDate(params.get("workedDateTo")); 
		
		return timesheetService.getTimesheetList(userId, workedDateFrom, workedDateTo);
	}
	
	@GetMapping("/{id}")
	public TimesheetResp getTimesheet(@PathVariable String id){
		return timesheetService.getTimesheet(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public TimesheetResp addTimesheet(@Valid @RequestBody TimesheetReq timesheetReq) throws InvalidDataException {
		return timesheetService.addTimesheet(timesheetReq);
	}
	
	@PutMapping("/{id}")
	public TimesheetResp updateTimesheet(@PathVariable String id, @RequestBody TimesheetReqUpd timesheetReqUpd) throws InvalidDataException {
		return timesheetService.updateTimesheet(id, timesheetReqUpd);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteTimesheet(@PathVariable String id) throws InvalidDataException {
		return timesheetService.deleteTimesheet(id);
	}
}
