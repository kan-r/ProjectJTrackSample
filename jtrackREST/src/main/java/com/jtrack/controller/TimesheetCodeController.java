package com.jtrack.controller;

import java.util.List;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.TimesheetCodeReq;
import com.jtrack.dto.TimesheetCodeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.TimesheetCodeService;

@RestController
@RequestMapping("/timesheetCodes")
public class TimesheetCodeController {
	
	@Autowired
	TimesheetCodeService timesheetCodeService;

	@GetMapping("")
	public List<TimesheetCodeResp> getTimesheetCodeList(){
		return timesheetCodeService.getTimesheetCodeList();
	}
	
	@GetMapping("/{id}")
	public TimesheetCodeResp getTimesheetCode(@PathVariable String id){
		return timesheetCodeService.getTimesheetCode(id);
	}

	@PostMapping("")
	@ResponseStatus(HttpStatus.CREATED)
	public TimesheetCodeResp addTimesheetCode(@Valid @RequestBody TimesheetCodeReq timesheetCodeReq) throws InvalidDataException {
		return timesheetCodeService.addTimesheetCode(timesheetCodeReq);
	}
	
	@PutMapping("/{id}")
	public TimesheetCodeResp updateTimesheetCode(@PathVariable String id, @RequestBody TimesheetCodeReq timesheetCodeReq) throws InvalidDataException {
		return timesheetCodeService.updateTimesheetCode(id, timesheetCodeReq);
	}
	
	@DeleteMapping("/{id}")
	public DeleteResp deleteTimesheetCode(@PathVariable String id) throws InvalidDataException {
		return timesheetCodeService.deleteTimesheetCode(id);
	}
}
