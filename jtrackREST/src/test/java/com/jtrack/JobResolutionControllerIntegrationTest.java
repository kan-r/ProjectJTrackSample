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

import com.jtrack.model.JobResolution;
import com.jtrack.model.User;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class JobResolutionControllerIntegrationTest {

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
		
		testEntityManager.persist(new JobResolution("Done", "Completed or fixed", true, null, "KAN", null, null));
		testEntityManager.persist(new JobResolution("Not Replicated", "Issue cannot be replicated", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobResolutionList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobResolution").value("Done"));
	}
	
	@Test
	void testGetJobResolutionList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobResolution() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions/Not Replicated")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobResolution").value("Not Replicated"));
	}
	
	@Test
	void testGetJobResolution_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions/Done")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetJobResolution_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobResolutions/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddJobResolution() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"TesT - 1_2\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobResolution").value("TesT - 1_2"));
	}

	@Test
	void testAddJobResolution_Unauthorized() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"TEST\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddJobResolution_JobResolution_Exists() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"Done\", \"jobResolutionDesc\": \"Completed or fixed\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionExistsMsg("Done")));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Null() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": null, \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Empty() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Blank() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \" \", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_Start_With_Invalid_Char() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"123\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_End_With_Invalid_Char() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T1@\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"TEST@10\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_1_Char() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_2_Chars() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T1\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobResolution").value("T1"));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_20_Chars() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T123456789T123456789\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobResolution").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJobResolution_JobResolution_With_21_Chars() throws Exception {
		
		String jobResolutionReq = "{\"jobResolution\": \"T123456789T1234567891\", \"jobResolutionDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobResolutions")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_RESOLUTION_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJobResolution() throws Exception {
		
		String jobResolutionReq = "{\"jobResolutionDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobResolutions/Done")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobResolutionDesc").value("Testing"));
	}
	
	@Test
	void testUpdateJobResolution_Unauthorized() throws Exception {
		
		String jobResolutionReq = "{\"jobResolutionDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobResolutions/Done")
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateJobResolution_JobResolution_Does_Not_Exist() throws Exception {
		
		String jobResolutionReq = "{\"jobResolutionDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobResolutions/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobResolutionReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionDoesNotExistMsg("Unknown")));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJobResolution() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobResolutions/Done")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionDeletedMsg("Done")));
	}
	
	@Test
	void testDeleteJobResolution_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobResolutions/Done")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteJobResolution_JobResolution_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobResolutions/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobResolutionDoesNotExistMsg("Unknown")));
	}

}
