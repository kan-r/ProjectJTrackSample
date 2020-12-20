package com.jtrack.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.TimesheetReq;
import com.jtrack.dto.TimesheetReqUpd;
import com.jtrack.dto.TimesheetResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.TimesheetMapper;
import com.jtrack.model.Timesheet;
import com.jtrack.repository.TimesheetRepository;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

@Service
@Transactional
public class TimesheetServiceImpl implements TimesheetService {
	
	Logger logger = LogManager.getLogger(TimesheetServiceImpl.class);
	
	@Autowired
	private TimesheetRepository timesheetRepository;
	
	@Autowired
	private TimesheetMapper mapper;

	private List<Timesheet> getAll(){
		logger.info("getAll()");
		return timesheetRepository.findAll(Sort.by("userId", "jobNo", "workedDate"));
	}
	
	private List<Timesheet> getAll(String userId, LocalDate workedDateFrom, LocalDate workedDateTo){
		
		logger.info("getAll({}, {}, {})", userId, workedDateFrom, workedDateTo);
		
		return timesheetRepository.findAll(new Specification<Timesheet>() {

			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<Timesheet> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
				
				List<Predicate> predicates = new ArrayList<>();
				if(userId != null && !userId.isEmpty()){
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("userId"), userId)));
				}
				if(workedDateFrom != null){
					predicates.add(criteriaBuilder.and(criteriaBuilder.greaterThanOrEqualTo(root.get("workedDate"), workedDateFrom)));
					
				}
				if(workedDateTo != null){
					predicates.add(criteriaBuilder.and(criteriaBuilder.lessThanOrEqualTo(root.get("workedDate"), workedDateTo)));
				}
				
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		}, Sort.by("userId", "jobNo", "workedDate"));
	}
	
	private Timesheet get(String timesheetId){
		logger.info("get({})", timesheetId);
		Optional<Timesheet> timesheet = timesheetRepository.findById(timesheetId);
		if(timesheet.isPresent()) {
			return timesheet.get();
		}
		
		return null;
	}
	
	private Timesheet add(Timesheet timesheet) throws InvalidDataException {
		logger.info("add({})", timesheet);
		
		String dtFormatted = GenUtils.formatDate(timesheet.getWorkedDate(), "yyyyMMdd");
        String timesheetId = timesheet.getUserId() + "-" + timesheet.getJobNo() + "-" + dtFormatted;
		
		if(timesheetExists(timesheetId)) {
			throw new InvalidDataException(
					ValidationUtils.timesheetExistsMsg(timesheetId)
			);
		}
		
        timesheet.setTimesheetId(timesheetId);
        timesheet.setUserCrt(GenUtils.getCurrentUserId());
		timesheet.setDateCrt(LocalDateTime.now());
		
		Timesheet t = timesheetRepository.save(timesheet);
		refreshJob(timesheet.getJobNo());
		return t;
	}
	
	private Timesheet update(String timesheetId, Timesheet timesheet) throws InvalidDataException {
		logger.info("update({}, {})", timesheetId, timesheet);
		
		if(!timesheetExists(timesheetId)) {
			throw new InvalidDataException(
					ValidationUtils.timesheetDoesNotExistMsg(timesheetId)
			);
		}
		
		timesheet.setUserMod(GenUtils.getCurrentUserId());
		timesheet.setDateMod(LocalDateTime.now());
		
		Timesheet t = timesheetRepository.save(timesheet);
		refreshJob(timesheet.getJobNo());
		return t;
	}
	
	private void delete(String timesheetId) throws InvalidDataException {
		logger.info("delete({})", timesheetId);
		
		if(!timesheetExists(timesheetId)) {
			throw new InvalidDataException(
					ValidationUtils.timesheetDoesNotExistMsg(timesheetId)
			);
		}
		
		long jobNo = get(timesheetId).getJobNo();
		
		timesheetRepository.deleteById(timesheetId);
		refreshJob(jobNo);
	}
	
	private boolean timesheetExists(String timesheetId) {
		Timesheet timesheetExisting = get(timesheetId);
		return (timesheetExisting != null);
	}
	
	private void refreshJob(long jobNo) {
		try {
			timesheetRepository.refreshCompletedHrsInJob(jobNo);
			timesheetRepository.refreshCompletedHrsInParentJob(jobNo);
			timesheetRepository.refreshStatusInJob(jobNo);
			timesheetRepository.refreshStatusInParentJob(jobNo);
		}catch(Exception e) {
			
		}
	}
	
	//Interface Implementations | Conversion to DTO :---

	@Override
	public List<TimesheetResp> getTimesheetList() {
		return mapper.convertToDto(
				getAll()
		);
	}
	
	@Override
	public List<TimesheetResp> getTimesheetList(String userId, LocalDate workedDateFrom, LocalDate workedDateTo)
			throws InvalidDataException {
		return mapper.convertToDto(
				getAll(userId, workedDateFrom, workedDateTo)
		);
	}

	@Override
	public TimesheetResp getTimesheet(String timesheetId) {
		return mapper.convertToDto(
				get(timesheetId)
		);
	}

	@Override
	public TimesheetResp addTimesheet(TimesheetReq timesheetReq) throws InvalidDataException {
		return mapper.convertToDto(
				add(mapper.convertToModel(timesheetReq))
		);
	}

	@Override
	public TimesheetResp updateTimesheet(String timesheetId, TimesheetReqUpd timesheetReqUpd) throws InvalidDataException {
		return mapper.convertToDto(
				update(timesheetId, mapper.convertToModel(timesheetReqUpd, get(timesheetId)))
		);
	}

	@Override
	public DeleteResp deleteTimesheet(String timesheetId) throws InvalidDataException {
		delete(timesheetId);
		return new DeleteResp(ValidationUtils.timesheetDeletedMsg(timesheetId));
	}
}
