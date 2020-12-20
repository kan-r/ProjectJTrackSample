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

import com.jtrack.model.JobPriority;
import com.jtrack.model.User;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class JobPriorityControllerIntegrationTest {

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
		
		testEntityManager.persist(new JobPriority("High", "High Priority", true, null, "KAN", null, null));
		testEntityManager.persist(new JobPriority("Low", "Low Priority", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriorityList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobPriority").value("High"));
	}
	
	@Test
	void testGetJobPriorityList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobPriority() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities/High")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobPriority").value("High"));
	}
	
	@Test
	void testGetJobPriority_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities/High")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetJobPriority_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobPriorities/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddJobPriority() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"TesT - 1_2\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobPriority").value("TesT - 1_2"));
	}

	@Test
	void testAddJobPriority_Unauthorized() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"Test\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddJobPriority_JobPriority_Exists() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"High\", \"jobPriorityDesc\": \"High\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityExistsMsg("High")));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Null() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": null, \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Empty() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Blank() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \" \", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_Start_With_Invalid_Char() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"123\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_End_With_Invalid_Char() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T1@\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"TEST@10\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_1_Char() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_2_Chars() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T1\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobPriority").value("T1"));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_20_Chars() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T123456789T123456789\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobPriority").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobPriority_JobPriority_With_21_Chars() throws Exception {
		
		String jobPriorityReq = "{\"jobPriority\": \"T123456789T1234567891\", \"jobPriorityDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobPriorities")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_PRIORITY_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobPriority() throws Exception {
		
		String jobPriorityReq = "{\"jobPriorityDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobPriorities/High")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobPriorityDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobPriority_Unauthorized() throws Exception {
		
		String jobPriorityReq = "{\"jobPriorityDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobPriorities/High")
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateJobPriority_JobPriority_Does_Not_Exist() throws Exception {
		
		String jobPriorityReq = "{\"jobPriorityDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobPriorities/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobPriorityReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityDoesNotExistMsg("Unknown")));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobPriority() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobPriorities/High")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityDeletedMsg("High")));
	}
	
	@Test
	void testDeleteJobPriority_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobPriorities/High")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteJobPriority_JobPriority_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobPriorities/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobPriorityDoesNotExistMsg("Unknown")));
	}

}
