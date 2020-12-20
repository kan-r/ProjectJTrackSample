package com.jtrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.jtrack.dto.TimesheetReq;
import com.jtrack.dto.TimesheetReqUpd;
import com.jtrack.dto.TimesheetResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.TimesheetMapper;
import com.jtrack.model.Job;
import com.jtrack.model.Timesheet;
import com.jtrack.model.User;
import com.jtrack.util.GenUtils;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class TimesheetServiceTest {
	
	@Autowired
	private TimesheetService timesheetService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	private long jobNo;
	
	@TestConfiguration
    static class TimesheetServiceConfiguration {
		
		@Bean
		public TimesheetService timesheetService() {
			return new TimesheetServiceImpl();
		}
		
		@Bean 
		public TimesheetMapper timesheetMapper() {
			return new TimesheetMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		Job job = testEntityManager.persist(new Job("JTrack - Bug", "Job Tracking System - Bug", "Adhoc", null, true, null, "KAN"));
		jobNo = job.getJobNo();
		
		String timesheetId = "KAN-" + jobNo + "-20201129";
		testEntityManager.persist(new Timesheet(timesheetId, "KAN", jobNo, GenUtils.toDate("2020-11-29"), 3, null, true, null, "KAN"));
		
		timesheetId = "KAN-" + jobNo + "-20201130";
		testEntityManager.persist(new Timesheet(timesheetId, "KAN", jobNo, GenUtils.toDate("2020-11-30"), 2, null, true, null, "KAN"));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetList() {
		assertThat(timesheetService.getTimesheetList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetListByParams() throws InvalidDataException {
		
		String userId = "KAN"; 
		LocalDate workedDateFrom = GenUtils.toDate("2020-11-29"); 
		LocalDate workedDateTo = null;
		
		assertThat(timesheetService.getTimesheetList(userId, workedDateFrom, workedDateTo)).hasSize(2);
		
		//---------------------------------------------------------------------
		
		userId = "KAN"; 
		workedDateFrom = GenUtils.toDate("2020-11-29"); 
		workedDateTo = GenUtils.toDate("2020-11-29");
		
		assertThat(timesheetService.getTimesheetList(userId, workedDateFrom, workedDateTo)).hasSize(1);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheet() {
		
		String id = "KAN-" + jobNo + "-20201129";
		assertThat(timesheetService.getTimesheet(id).getTimesheetId()).isEqualTo(id);
	}
	
	@Test
	void testGetTimesheet_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(timesheetService.getTimesheet(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddTimesheet() {
		
		String id = "KAN-" + jobNo + "-20201201";
		
		TimesheetReq timesheetReq = new TimesheetReq();
		timesheetReq.setUserId("KAN");;
		timesheetReq.setJobNo(jobNo);
		
		try {
			timesheetReq.setWorkedDate(GenUtils.toDate("2020-12-01"));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
		
		timesheetReq.setWorkedHrs(3.0);
		timesheetReq.setTimesheetCode("ZZ11");
		timesheetReq.setActive(true);
		
		try {
			assertThat(timesheetService.addTimesheet(timesheetReq).getTimesheetId()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddTimesheet_Timesheet_Exists() {
		
		String id = "KAN-" + jobNo + "-20201129";
		
		TimesheetReq timesheetReq = new TimesheetReq();
		timesheetReq.setUserId("KAN");;
		timesheetReq.setJobNo(jobNo);
		
		try {
			timesheetReq.setWorkedDate(GenUtils.toDate("2020-11-29"));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
		
		timesheetReq.setWorkedHrs(3.0);
		timesheetReq.setTimesheetCode("ZZ11");
		timesheetReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			timesheetService.addTimesheet(timesheetReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.timesheetExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateTimesheet() {
		
		String id = "KAN-" + jobNo + "-20201129";
		
		TimesheetReqUpd timesheetReq = new TimesheetReqUpd();
		timesheetReq.setWorkedHrs(5.0);
		timesheetReq.setTimesheetCode("ZZ11");
		timesheetReq.setActive(true);
		
		try {
			TimesheetResp timesheetResp = timesheetService.updateTimesheet(id, timesheetReq);
			
			assertThat(timesheetResp.getTimesheetId()).isEqualTo(id);
			assertThat(timesheetResp.getWorkedHrs()).isEqualTo(5.0);
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateTimesheet_Timesheet_Does_Not_Exist() {
		
		String id = "Unknown";
		
		TimesheetReqUpd timesheetReq = new TimesheetReqUpd();
		timesheetReq.setWorkedHrs(5.0);
		timesheetReq.setTimesheetCode("ZZ11");
		timesheetReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			timesheetService.updateTimesheet(id, timesheetReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.timesheetDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteTimesheet() {
		
		String id = "KAN-" + jobNo + "-20201129";
		
		try {
			assertThat(timesheetService.deleteTimesheet(id).getMessage()).isEqualTo(ValidationUtils.timesheetDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteTimesheet_Timesheet_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			timesheetService.deleteTimesheet(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.timesheetDoesNotExistMsg(id));
	}

}
