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

import com.jtrack.dto.JobPriorityReq;
import com.jtrack.dto.JobPriorityResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobPriorityMapper;
import com.jtrack.model.JobPriority;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class JobPriorityServiceTest {
	
	@Autowired
	private JobPriorityService jobPriorityService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class JobPriorityServiceConfiguration {
		
		@Bean
		public JobPriorityService jobPriorityService() {
			return new JobPriorityServiceImpl();
		}
		
		@Bean 
		public JobPriorityMapper jobPriorityMapper() {
			return new JobPriorityMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobPriority("High", "High Priority", true, null, "KAN", null, null));
		testEntityManager.persist(new JobPriority("Low", "Low Priority", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriorityList() {
		assertThat(jobPriorityService.getJobPriorityList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriority() {
		
		String id = "High";
		assertThat(jobPriorityService.getJobPriority(id).getJobPriority()).isEqualTo(id);
	}
	
	@Test
	void testGetJobPriority_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(jobPriorityService.getJobPriority(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobPriority() {
		
		String id = "Test";
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriority(id);
		jobPriorityReq.setJobPriorityDesc("Test");
		jobPriorityReq.setActive(true);
		
		try {
			assertThat(jobPriorityService.addJobPriority(jobPriorityReq).getJobPriority()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddJobPriority_JobPriority_Exists() {
		
		String id = "High";
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriority(id);
		jobPriorityReq.setJobPriorityDesc("High Priority");
		jobPriorityReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobPriorityService.addJobPriority(jobPriorityReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobPriorityExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobPriority() {
		
		String id = "High";
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriorityDesc("Testing");
		jobPriorityReq.setActive(true);
		
		try {
			JobPriorityResp jobPriorityResp = jobPriorityService.updateJobPriority(id, jobPriorityReq);
			
			assertThat(jobPriorityResp.getJobPriority()).isEqualTo(id);
			assertThat(jobPriorityResp.getJobPriorityDesc()).isEqualTo("Testing");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateJobPriority_JobPriority_Does_Not_Exist() {
		
		String id = "Unknown";
		
		JobPriorityReq jobPriorityReq = new JobPriorityReq();
		jobPriorityReq.setJobPriorityDesc("Testing");
		jobPriorityReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobPriorityService.updateJobPriority(id, jobPriorityReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobPriorityDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteJobPriority() {
		
		String id = "High";
		
		try {
			assertThat(jobPriorityService.deleteJobPriority(id).getMessage()).isEqualTo(ValidationUtils.jobPriorityDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteJobPriority_JobPriority_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobPriorityService.deleteJobPriority(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobPriorityDoesNotExistMsg(id));
	}

}
