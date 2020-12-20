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

import com.jtrack.model.JobType;
import com.jtrack.model.User;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class JobTypeControllerIntegrationTest {

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
		
		testEntityManager.persist(new JobType("Project", "Job to group some tasks as a Project", true, null, "KAN", null, null));
		testEntityManager.persist(new JobType("Task", "Task is part of a project", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobTypeList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobType").value("Project"));
	}
	
	@Test
	void testGetJobTypeList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobType() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes/Task")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobType").value("Task"));
	}
	
	@Test
	void testGetJobType_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes/Task")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetJobType_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobTypes/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddJobType() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"TesT - 1_2\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobType").value("TesT - 1_2"));
	}

	@Test
	void testAddJobType_Unauthorized() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"Test\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddJobType_JobType_Exists() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"Task\", \"jobTypeDesc\": \"Task\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeExistsMsg("Task")));
	}
	
	@Test
	void testAddJobType_JobType_Null() throws Exception {
		
		String jobTypeReq = "{\"jobType\": null, \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_Empty() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_Blank() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \" \", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_Start_With_Invalid_Char() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"123\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_End_With_Invalid_Char() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T1@\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"TEST@10\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_With_1_Char() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	@Test
	void testAddJobType_JobType_With_2_Chars() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T1\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobType").value("T1"));
	}
	
	@Test
	void testAddJobType_JobType_With_20_Chars() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T123456789T123456789\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobType").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobType_JobType_With_21_Chars() throws Exception {
		
		String jobTypeReq = "{\"jobType\": \"T123456789T1234567891\", \"jobTypeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobTypes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_TYPE_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobType() throws Exception {
		
		String jobTypeReq = "{\"jobTypeDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobTypes/Task")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobTypeDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobType_Unauthorized() throws Exception {
		
		String jobTypeReq = "{\"jobTypeDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobTypes/Task")
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateJobType_JobType_Does_Not_Exist() throws Exception {
		
		String jobTypeReq = "{\"jobTypeDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobTypes/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobTypeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeDoesNotExistMsg("Unknown")));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobType() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobTypes/Task")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeDeletedMsg("Task")));
	}
	
	@Test
	void testDeleteJobType_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobTypes/Task")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteJobType_JobType_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobTypes/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobTypeDoesNotExistMsg("Unknown")));
	}

}
