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
import com.jtrack.model.Timesheet;
import com.jtrack.model.User;
import com.jtrack.util.GenUtils;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class TimesheetControllerIntegrationTest {

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
		
		Job job = testEntityManager.persist(new Job("JTrack - Bug", "Job Tracking System - Bug", "Adhoc", null, true, null, "KAN"));
		jobNo = job.getJobNo();
		
		String timesheetId = "KAN-" + jobNo + "-20201129";
		testEntityManager.persist(new Timesheet(timesheetId, "KAN", jobNo, GenUtils.toDate("2020-11-29"), 3, null, true, null, "KAN"));
		
		timesheetId = "KAN-" + jobNo + "-20201130";
		testEntityManager.persist(new Timesheet(timesheetId, "KAN", jobNo, GenUtils.toDate("2020-11-30"), 2, null, true, null, "KAN"));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].workedHrs").value(3));
	}
	
	@Test
	void testGetTimesheetList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetListByParams() throws Exception {
		
		String params = "userId=KAN&workedDateFrom=2020-11-29&workedDateTo=";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets?" + params)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[1].workedHrs").value(2));
		
		//--------------------------------------------------------------------
		
		params = "userId=KAN&workedDateFrom=2020-11-29&workedDateTo=2020-11-29";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets?" + params)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(1)))
				.andExpect(jsonPath("$[0].workedHrs").value(3));
	}
	
	@Test
	void testGetTimesheetListByParams_Unauthorized() throws Exception {
		
		String params = "userId=KAN&workedDateFrom=2020-11-29&workedDateTo=";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets?" + params)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheet() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201129";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets/{id}", id)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetId").value(id));
	}
	
	@Test
	void testGetTimesheet_Unauthorized() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201129";
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetTimesheet_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheets/{id}", "Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddTimesheet() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201201";
		String timesheetReq = "{\"userId\": \"KAN\", \"jobNo\": " + jobNo + ", \"workedDate\": \"2020-12-01\", \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetId").value(id));
	}

	@Test
	void testAddTimesheet_Unauthorized() throws Exception {
		
		String timesheetReq = "{\"userId\": \"KAN\", \"jobNo\": " + jobNo + ", \"workedDate\": \"2020-12-01\", \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddTimesheet_Timesheet_Exists() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201129";
		String timesheetReq = "{\"userId\": \"KAN\", \"jobNo\": " + jobNo + ", \"workedDate\": \"2020-11-29\", \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetExistsMsg(id)));
	}
	
	@Test
	void testAddTimesheet_Timesheet_Null() throws Exception {
		
		String timesheetReq = "{\"userId\": null, \"jobNo\": null, \"workedDate\": null, \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTBLANK_TIMESHEET_MSG));
	}
	
	@Test
	void testAddTimesheet_Timesheet_Blank() throws Exception {
		
		String timesheetReq = "{\"userId\": \"\", \"jobNo\": \"\", \"workedDate\": \"\", \"workedHrs\": 3.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheets")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTBLANK_TIMESHEET_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateTimesheet() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201129";
		String timesheetReq = "{\"workedHrs\": 5.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheets/{id}", id)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetCode").value("ZZ11"))
				.andExpect(jsonPath("$.workedHrs").value(5));
	}
	
	@Test
	void testUpdateTimesheet_Unauthorized() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201129";
		String timesheetReq = "{\"workedHrs\": 5.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheets/{id}", id)
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateTimesheet_Timesheet_Does_Not_Exist() throws Exception {
		
		String timesheetReq = "{\"workedHrs\": 5.0, \"timesheetCode\": \"ZZ11\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheets/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetDoesNotExistMsg("Unknown")));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteTimesheet() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201129";
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheets/{id}", id)
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetDeletedMsg(id)));
	}
	
	@Test
	void testDeleteTimesheet_Unauthorized() throws Exception {
		
		String id = "KAN-" + jobNo + "-20201129";
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheets/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteTimesheet_Timesheet_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheets/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetDoesNotExistMsg("Unknown")));
	}

}
