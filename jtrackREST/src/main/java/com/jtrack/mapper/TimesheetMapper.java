package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.TimesheetReq;
import com.jtrack.dto.TimesheetReqUpd;
import com.jtrack.dto.TimesheetResp;
import com.jtrack.model.Timesheet;

@Component
public class TimesheetMapper {
	
	@Autowired
    private ModelMapper modelMapper;
		
	public Timesheet convertToModel(TimesheetReq timesheetReq) {
		if(timesheetReq == null) {
			return null;
		}
		
        return modelMapper.map(timesheetReq, Timesheet.class);
    }
	
	public Timesheet convertToModel(TimesheetReqUpd timesheetReqUpd, Timesheet timesheet) {
		if(timesheetReqUpd == null || timesheet == null) {
			return null;
		}
		
		modelMapper.map(timesheetReqUpd, timesheet);
		return timesheet;
	}
	
	public TimesheetResp convertToDto(Timesheet timesheet) {
		if(timesheet == null) {
			return null;
		}
		
		return modelMapper.map(timesheet, TimesheetResp.class);
    }
	
	public List<TimesheetResp> convertToDto(List<Timesheet> timesheetList){
		if(timesheetList == null) {
			return null;
		}
		
		return timesheetList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
