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

import com.jtrack.model.JobStage;
import com.jtrack.model.User;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class JobStageControllerIntegrationTest {

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
		
		testEntityManager.persist(new JobStage("Design", "Requirements gathering and high level design", true, null, "KAN", null, null));
		testEntityManager.persist(new JobStage("Development", "Detailed level design, coding and unit testing", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStageList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobStage").value("Design"));
	}
	
	@Test
	void testGetJobStageList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobStage() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages/Design")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStage").value("Design"));
	}
	
	@Test
	void testGetJobStage_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages/Design")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetJobStage_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobStages/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddJobStage() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"TesT - 1_2\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStage").value("TesT - 1_2"));
	}

	@Test
	void testAddJobStage_Unauthorized() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"Test\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddJobStage_JobStage_Exists() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"Design\", \"jobStageDesc\": \"High level design\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageExistsMsg("Design")));
	}
	
	@Test
	void testAddJobStage_JobStage_Null() throws Exception {
		
		String jobStageReq = "{\"jobStage\": null, \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_Empty() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_Blank() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \" \", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_Start_With_Invalid_Char() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"123\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_End_With_Invalid_Char() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T1@\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"TEST@10\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_With_1_Char() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	@Test
	void testAddJobStage_JobStage_With_2_Chars() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T1\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStage").value("T1"));
	}
	
	@Test
	void testAddJobStage_JobStage_With_20_Chars() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T123456789T123456789\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobStage").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobStage_JobStage_With_21_Chars() throws Exception {
		
		String jobStageReq = "{\"jobStage\": \"T123456789T1234567891\", \"jobStageDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobStages")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_STAGE_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobStage() throws Exception {
		
		String jobStageReq = "{\"jobStageDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStages/Design")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobStageDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobStage_Unauthorized() throws Exception {
		
		String jobStageReq = "{\"jobStageDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStages/Design")
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateJobStage_JobStage_Does_Not_Exist() throws Exception {
		
		String jobStageReq = "{\"jobStageDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobStages/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobStageReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageDoesNotExistMsg("Unknown")));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobStage() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStages/Design")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageDeletedMsg("Design")));
	}
	
	@Test
	void testDeleteJobStage_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStages/Design")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteJobStage_JobStage_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobStages/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobStageDoesNotExistMsg("Unknown")));
	}

}
