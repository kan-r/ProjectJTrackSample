package com.jtrack.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.TimesheetCodeReq;
import com.jtrack.dto.TimesheetCodeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.TimesheetCodeMapper;
import com.jtrack.model.TimesheetCode;
import com.jtrack.repository.TimesheetCodeRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class TimesheetCodeServiceImpl implements TimesheetCodeService {
	
	Logger logger = LogManager.getLogger(TimesheetCodeServiceImpl.class);
	
	@Autowired
	private TimesheetCodeRepository timesheetCodeRepository;
	
	@Autowired
	private TimesheetCodeMapper mapper;

	private List<TimesheetCode> getAll(){
		logger.info("getAll()");
		return timesheetCodeRepository.findAll(Sort.by("timesheetCode"));
	}
	
	private TimesheetCode get(String timesheetCodeId){
		logger.info("get({})", timesheetCodeId);
		Optional<TimesheetCode> timesheetCode = timesheetCodeRepository.findById(timesheetCodeId);
		if(timesheetCode.isPresent()) {
			return timesheetCode.get();
		}
		
		return null;
	}
	
	private TimesheetCode add(TimesheetCode timesheetCode) throws InvalidDataException {
		logger.info("add({})", timesheetCode);
		
		if(timesheetCodeExists(timesheetCode.getTimesheetCode())) {
			throw new InvalidDataException(
					ValidationUtils.timesheetCodeExistsMsg(timesheetCode.getTimesheetCode())
			);
		}
		
		timesheetCode.setUserCrt(GenUtils.getCurrentUserId());
		timesheetCode.setDateCrt(LocalDateTime.now());
		 
	    return timesheetCodeRepository.save(timesheetCode);
	}
	
	private TimesheetCode update(String timesheetCodeId, TimesheetCode timesheetCode) throws InvalidDataException {
		logger.info("update({}, {})", timesheetCodeId, timesheetCode);
		
		if(!timesheetCodeExists(timesheetCodeId)) {
			throw new InvalidDataException(
					ValidationUtils.timesheetCodeDoesNotExistMsg(timesheetCodeId)
			);
		}
		
		timesheetCode.setUserMod(GenUtils.getCurrentUserId());
		timesheetCode.setDateMod(LocalDateTime.now());
		
		return timesheetCodeRepository.save(timesheetCode);
	}
	
	private void delete(String timesheetCodeId) throws InvalidDataException {
		logger.info("delete({})", timesheetCodeId);
		
		if(!timesheetCodeExists(timesheetCodeId)) {
			throw new InvalidDataException(
					ValidationUtils.timesheetCodeDoesNotExistMsg(timesheetCodeId)
			);
		}
		
		timesheetCodeRepository.deleteById(timesheetCodeId);
	}
	
	private boolean timesheetCodeExists(String timesheetCodeId) {
		TimesheetCode timesheetCodeExisting = get(timesheetCodeId);
		return (timesheetCodeExisting != null);
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<TimesheetCodeResp> getTimesheetCodeList() {
		return mapper.convertToDto(
				getAll()
		);
	}

	@Override
	public TimesheetCodeResp getTimesheetCode(String timesheetCodeId) {
		return mapper.convertToDto(
				get(timesheetCodeId)
		);
	}

	@Override
	public TimesheetCodeResp addTimesheetCode(TimesheetCodeReq timesheetCodeReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(timesheetCodeReq))
		);
	}

	@Override
	public TimesheetCodeResp updateTimesheetCode(String timesheetCodeId, TimesheetCodeReq timesheetCodeReq) throws InvalidDataException {
		timesheetCodeReq.setTimesheetCode(timesheetCodeId);
		return mapper.convertToDto(
				update(timesheetCodeId, mapper.convertToModel(timesheetCodeReq, get(timesheetCodeId)))
		);
	}

	@Override
	public DeleteResp deleteTimesheetCode(String timesheetCodeId) throws InvalidDataException {
		delete(timesheetCodeId);
		return new DeleteResp(ValidationUtils.timesheetCodeDeletedMsg(timesheetCodeId));
	}
}
