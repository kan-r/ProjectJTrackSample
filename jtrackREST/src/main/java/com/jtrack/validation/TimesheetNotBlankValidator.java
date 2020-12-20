package com.jtrack.validation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.jtrack.dto.TimesheetReq;

public class TimesheetNotBlankValidator implements ConstraintValidator<TimesheetNotBlank, TimesheetReq> {

	@Override
	public boolean isValid(TimesheetReq timesheetReq, ConstraintValidatorContext context) {
		if(timesheetReq.getUserId() == null || timesheetReq.getUserId().isBlank() || timesheetReq.getJobNo() <= 0  || timesheetReq.getWorkedDate() == null) {
			return false;
		}
		
		return true;
	}

}