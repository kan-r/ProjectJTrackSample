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

import com.jtrack.dto.JobResolutionReq;
import com.jtrack.dto.JobResolutionResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.JobResolutionMapper;
import com.jtrack.model.JobResolution;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class JobResolutionServiceTest {
	
	@Autowired
	private JobResolutionService jobResolutionService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class JobResolutionServiceConfiguration {
		
		@Bean
		public JobResolutionService jobResolutionService() {
			return new JobResolutionServiceImpl();
		}
		
		@Bean 
		public JobResolutionMapper jobResolutionMapper() {
			return new JobResolutionMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobResolution("Done", "Completed or fixed", true, null, "KAN", null, null));
		testEntityManager.persist(new JobResolution("Not Replicated", "Issue cannot be replicated", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobResolutionList() {
		assertThat(jobResolutionService.getJobResolutionList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobResolution() {
		
		String id = "Done";
		assertThat(jobResolutionService.getJobResolution(id).getJobResolution()).isEqualTo(id);
	}
	
	@Test
	void testGetJobResolution_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(jobResolutionService.getJobResolution(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddJobResolution() {
		
		String id = "Test";
		
		JobResolutionReq jobResolutionReq = new JobResolutionReq();
		jobResolutionReq.setJobResolution(id);
		jobResolutionReq.setJobResolutionDesc("Test");
		jobResolutionReq.setActive(true);
		
		try {
			assertThat(jobResolutionService.addJobResolution(jobResolutionReq).getJobResolution()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddJobResolution_JobResolution_Exists() {
		
		String id = "Done";
		
		JobResolutionReq jobResolutionReq = new JobResolutionReq();
		jobResolutionReq.setJobResolution(id);
		jobResolutionReq.setJobResolutionDesc("Completed or fixed");
		jobResolutionReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobResolutionService.addJobResolution(jobResolutionReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobResolutionExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobResolution() {
		
		String id = "Done";
		
		JobResolutionReq jobResolutionReq = new JobResolutionReq();
		jobResolutionReq.setJobResolutionDesc("Testing");
		jobResolutionReq.setActive(true);
		
		try {
			JobResolutionResp jobResolutionResp = jobResolutionService.updateJobResolution(id, jobResolutionReq);
			
			assertThat(jobResolutionResp.getJobResolution()).isEqualTo(id);
			assertThat(jobResolutionResp.getJobResolutionDesc()).isEqualTo("Testing");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateJobResolution_JobResolution_Does_Not_Exist() {
		
		String id = "Unknown";
		
		JobResolutionReq jobResolutionReq = new JobResolutionReq();
		jobResolutionReq.setJobResolutionDesc("Testing");
		jobResolutionReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobResolutionService.updateJobResolution(id, jobResolutionReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobResolutionDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteJobResolution() {
		
		String id = "Done";
		
		try {
			assertThat(jobResolutionService.deleteJobResolution(id).getMessage()).isEqualTo(ValidationUtils.jobResolutionDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteJobResolution_JobResolution_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			jobResolutionService.deleteJobResolution(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.jobResolutionDoesNotExistMsg(id));
	}

}
