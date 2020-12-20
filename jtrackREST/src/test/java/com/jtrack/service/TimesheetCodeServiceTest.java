package com.jtrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.jtrack.dto.TimesheetCodeReq;
import com.jtrack.dto.TimesheetCodeResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.TimesheetCodeMapper;
import com.jtrack.model.TimesheetCode;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class TimesheetCodeServiceTest {
	
	@Autowired
	private TimesheetCodeService timesheetCodeService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class TimesheetCodeServiceConfiguration {
		
		@Bean
		public TimesheetCodeService timesheetCodeService() {
			return new TimesheetCodeServiceImpl();
		}
		
		@Bean 
		public TimesheetCodeMapper timesheetCodeMapper() {
			return new TimesheetCodeMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new TimesheetCode("ZZ01", "Reviews/Meetings", true, null, "KAN", null, null));
		testEntityManager.persist(new TimesheetCode("ZZ11", "Sales Support", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetCodeList() {
		assertThat(timesheetCodeService.getTimesheetCodeList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetCode() {
		
		String id = "ZZ01";
		assertThat(timesheetCodeService.getTimesheetCode(id).getTimesheetCode()).isEqualTo(id);
	}
	
	@Test
	void testGetTimesheetCode_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(timesheetCodeService.getTimesheetCode(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddTimesheetCode() {
		
		String id = "Test";
		
		TimesheetCodeReq timesheetCodeReq = new TimesheetCodeReq();
		timesheetCodeReq.setTimesheetCode(id);
		timesheetCodeReq.setTimesheetCodeDesc("Test");
		timesheetCodeReq.setActive(true);
		
		try {
			assertThat(timesheetCodeService.addTimesheetCode(timesheetCodeReq).getTimesheetCode()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Exists() {
		
		String id = "ZZ01";
		
		TimesheetCodeReq timesheetCodeReq = new TimesheetCodeReq();
		timesheetCodeReq.setTimesheetCode(id);
		timesheetCodeReq.setTimesheetCodeDesc("Reviews/Meetings");
		timesheetCodeReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			timesheetCodeService.addTimesheetCode(timesheetCodeReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.timesheetCodeExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateTimesheetCode() {
		
		String id = "ZZ01";
		
		TimesheetCodeReq timesheetCodeReq = new TimesheetCodeReq();
		timesheetCodeReq.setTimesheetCodeDesc("Testing");
		timesheetCodeReq.setActive(true);
		
		try {
			TimesheetCodeResp timesheetCodeResp = timesheetCodeService.updateTimesheetCode(id, timesheetCodeReq);
			
			assertThat(timesheetCodeResp.getTimesheetCode()).isEqualTo(id);
			assertThat(timesheetCodeResp.getTimesheetCodeDesc()).isEqualTo("Testing");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateTimesheetCode_TimesheetCode_Does_Not_Exist() {
		
		String id = "Unknown";
		
		TimesheetCodeReq timesheetCodeReq = new TimesheetCodeReq();
		timesheetCodeReq.setTimesheetCodeDesc("Testing");
		timesheetCodeReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			timesheetCodeService.updateTimesheetCode(id, timesheetCodeReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.timesheetCodeDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteTimesheetCode() {
		
		String id = "ZZ01";
		
		try {
			assertThat(timesheetCodeService.deleteTimesheetCode(id).getMessage()).isEqualTo(ValidationUtils.timesheetCodeDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteTimesheetCode_TimesheetCode_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			timesheetCodeService.deleteTimesheetCode(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.timesheetCodeDoesNotExistMsg(id));
	}

}
