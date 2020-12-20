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

import com.jtrack.model.Job;
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
class JobControllerIntegrationTest {

	@Autowired
	private MockMvc mvc;
	
	@Autowired
    private WebApplicationContext context;
	
	@Autowired
    private FilterChainProxy filterChain;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	private long jobNo;
	
	@BeforeEach
	void setUp() throws Exception {
		
		mvc = MockMvcBuilders
				.webAppContextSetup(context)
				.addFilter(filterChain)
				.build();
		
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "KAN", null, null));
		
		testEntityManager.persist(new JobType("Project", "Job to group some tasks as a Project", true, null, "KAN", null, null));
		testEntityManager.persist(new JobType("Task", "Task is part of a project", true, null, "KAN", null, null));
		testEntityManager.persist(new JobType("Adhoc", "Ad hoc job", true, null, "KAN", null, null));
		
		Job job = testEntityManager.persist(new Job("JTrack", "Job Tracking System", "Project", null, true, null, "KAN"));
		testEntityManager.persist(new Job("JTrack - Job Functionality", "Develop JobService & JobController", "Task", job.getJobNo(), true, null, "KAN"));
		job = testEntityManager.persist(new Job("JTrack - Bug", "Job Tracking System - Bug", "Adhoc", null, true, null, "KAN"));
		
		jobNo = job.getJobNo();
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJobList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(3)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack"));
	}
	
	@Test
	void testGetJobList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testGetJobListByType() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?type=Project")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack"));
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?type=Bug")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}
	
	@Test
	void testGetJobListByType_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?type=Project")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testGetJobListByParams() throws Exception {
		
		String params = "name=JTrack&type=&status=&assignedTo=&includeChild=true&nameC=&typeC=&statusC=&assignedToC=";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?" + params)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack"))
				.andExpect(jsonPath("$[1].jobName").value("JTrack - Job Functionality"));
		
		params = "name=JTrack%&type=Adhoc&status=&assignedTo=&includeChild=true&nameC=&typeC=&statusC=&assignedToC=";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?" + params)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].jobName").value("JTrack - Bug"));
		
		params = "name=JTrack%&type=Adhoc&status=Scheduled&assignedTo=&includeChild=true&nameC=&typeC=&statusC=&assignedToC=";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?" + params)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(0)));
	}
	
	@Test
	void testGetJobListByParams_Unauthorized() throws Exception {
		
		String params = "name=JTrack&type=&status=&assignedTo=&includeChild=true&nameC=&typeC=&statusC=&assignedToC=";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs?" + params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetJob() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs/{id}", jobNo)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobName").value("JTrack - Bug"));
	}
	
	@Test
	void testGetJob_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs/{id}", jobNo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetJob_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/jobs/0")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddJob() throws Exception {
		
		String jobReq = "{\"jobName\": \"TesT - 1_2\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobName").value("TesT - 1_2"));
	}

	@Test
	void testAddJob_Unauthorized() throws Exception {
		
		String jobReq = "{\"jobName\": \"Test\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddJob_Job_Exists() throws Exception {
		
		String jobReq = "{\"jobName\": \"JTrack\", \"jobType\": \"Project\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobExistsMsg("JTrack")));
	}
	
	@Test
	void testAddJob_Job_Null() throws Exception {
		
		String jobReq = "{\"jobName\": null, \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_Empty() throws Exception {
		
		String jobReq = "{\"jobName\": \"\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_Blank() throws Exception {
		
		String jobReq = "{\"jobName\": \" \", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_Start_With_Invalid_Char() throws Exception {
		
		String jobReq = "{\"jobName\": \"123\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_End_With_Invalid_Char() throws Exception {
		
		String jobReq = "{\"jobName\": \"T1@\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String jobReq = "{\"jobName\": \"Test@10\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_With_1_Char() throws Exception {
		
		String jobReq = "{\"jobName\": \"T\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	@Test
	void testAddJob_Job_With_2_Chars() throws Exception {
		
		String jobReq = "{\"jobName\": \"T1\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobName").value("T1"));
	}
	
	@Test
	void testAddJob_Job_With_20_Chars() throws Exception {
		
		String jobReq = "{\"jobName\": \"T123456789T123456789\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.jobName").value("T123456789T123456789"));
	}
	
	@Test
	void testAddJob_Job_With_21_Chars() throws Exception {
		
		String jobReq = "{\"jobName\": \"T123456789T1234567891\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/jobs")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_JOB_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateJob() throws Exception {
		
		String jobReq = "{\"jobName\": \"Test\", \"jobType\": \"Project\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobs/{id}", jobNo)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.jobName").value("Test"));
	}
	
	@Test
	void testUpdateJob_Unauthorized() throws Exception {
		
		String jobReq = "{\"jobName\": \"Test\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobs/{id}", jobNo)
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateJob_Job_Does_Not_Exist() throws Exception {
		
		String jobReq = "{\"jobName\": \"Test\", \"jobType\": \"Adhoc\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/jobs/0")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(jobReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobDoesNotExistMsg(0)));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteJob() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobs/{id}", jobNo)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobDeletedMsg(jobNo)));
	}
	
	@Test
	void testDeleteJob_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobs/{id}", jobNo)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteJob_Job_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/jobs/0")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.jobDoesNotExistMsg(0)));
	}

}
