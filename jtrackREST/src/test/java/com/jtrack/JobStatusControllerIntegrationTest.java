package com.jtrack;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import javax.transaction.Transactional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.jtrack.model.JobStatus;
import com.jtrack.model.User;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class JobStatusControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
    private FilterChainProxy filterChain;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@BeforeEach
	void setUp() throws Exception {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilter(filterChain)
				.build();
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobStatus("In Progress", "Work in progress", true, null, "KAN", null, null));
		testEntityManager.persist(new JobStatus("Completed", "Completed with some resolution", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStatusList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobStatus").value("Completed"));
	}
	
	@Test
	void testGetJobStatusList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStatus() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses/Completed")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStatus").value("Completed"));
	}
	
	@Test
	void testGetJobStatus_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses/Completed")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetJobStatus_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStatuses/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddJobStatus() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"TesT - 1_2\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStatus").value("TesT - 1_2"));
	}

	@Test
	void testAddJobStatus_Unauthorized() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"Test\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddJobStatus_JobStatus_Exists() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"Completed\", \"jobStatusDesc\": \"High level design\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusExistsMsg("Completed")));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Null() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": null, \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Empty() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Blank() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \" \", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_Start_With_Invalid_Char() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"123\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_End_With_Invalid_Char() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T1@\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"TEST@10\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_1_Char() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_2_Chars() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T1\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStatus").value("T1"));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_20_Chars() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T123456789T123456789\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStatus").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobStatus_JobStatus_With_21_Chars() throws Exception {
		
		String jobStatusReq = "{\"jobStatus\": \"T123456789T1234567891\", \"jobStatusDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStatuses")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STATUS_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobStatus() throws Exception {
		
		String jobStatusReq = "{\"jobStatusDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStatuses/Completed")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStatusDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobStatus_Unauthorized() throws Exception {
		
		String jobStatusReq = "{\"jobStatusDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStatuses/Completed")
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateJobStatus_JobStatus_Does_Not_Exist() throws Exception {
		
		String jobStatusReq = "{\"jobStatusDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStatuses/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStatusReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusDoesNotExistMsg("Unknown")));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobStatus() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStatuses/Completed")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusDeletedMsg("Completed")));
	}
	
	@Test
	void testDeleteJobStatus_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStatuses/Completed")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteJobStatus_JobStatus_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStatuses/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStatusDoesNotExistMsg("Unknown")));
	}

}
