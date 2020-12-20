package com.jtrack.mapper;

import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.jtrack.dto.TimesheetCodeReq;
import com.jtrack.dto.TimesheetCodeResp;
import com.jtrack.model.TimesheetCode;

@Component
public class TimesheetCodeMapper {
	
	@Autowired
    private ModelMapper modelMapper;

	public TimesheetCode convertToModel(TimesheetCodeReq timesheetCodeReq) {
		if(timesheetCodeReq == null) {
			return null;
		}
		
        return modelMapper.map(timesheetCodeReq, TimesheetCode.class);
    }
	
	public TimesheetCode convertToModel(TimesheetCodeReq timesheetCodeReq, TimesheetCode timesheetCode) {
		if(timesheetCodeReq == null || timesheetCode == null) {
			return null;
		}
		
		modelMapper.map(timesheetCodeReq, timesheetCode);
		return timesheetCode;
	}
	
	public TimesheetCodeResp convertToDto(TimesheetCode timesheetCode) {
		if(timesheetCode == null) {
			return null;
		}
		
		return modelMapper.map(timesheetCode, TimesheetCodeResp.class);
    }
	
	public List<TimesheetCodeResp> convertToDto(List<TimesheetCode> timesheetCodeList){
		if(timesheetCodeList == null) {
			return null;
		}
		
		return timesheetCodeList
				.stream()
				.map(this::convertToDto)
				.collect(Collectors.toList());
	}
}
