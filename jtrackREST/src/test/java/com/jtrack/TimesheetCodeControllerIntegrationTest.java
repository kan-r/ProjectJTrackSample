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

import com.jtrack.model.TimesheetCode;
import com.jtrack.model.User;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class TimesheetCodeControllerIntegrationTest {

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
		
		testEntityManager.persist(new TimesheetCode("ZZ01", "Reviews/Meetings", true, null, "KAN", null, null));
		testEntityManager.persist(new TimesheetCode("ZZ11", "Sales Support", true, null, "KAN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetCodeList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[0].timesheetCode").value("ZZ01"));
	}
	
	@Test
	void testGetTimesheetCodeList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetTimesheetCode() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes/ZZ01")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetCode").value("ZZ01"));
	}
	
	@Test
	void testGetTimesheetCode_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes/ZZ01")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetTimesheetCode_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/timesheetCodes/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testAddTimesheetCode() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"TesT - 1_2\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetCode").value("TesT - 1_2"));
	}

	@Test
	void testAddTimesheetCode_Unauthorized() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"Test\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Exists() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"ZZ01\", \"timesheetCodeDesc\": \"Reviews/Meetings\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeExistsMsg("ZZ01")));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Null() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": null, \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Empty() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Blank() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \" \", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_Start_With_Invalid_Char() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"123\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_End_With_Invalid_Char() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T1@\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"TEST@10\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_1_Char() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_2_Chars() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T1\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetCode").value("T1"));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_20_Chars() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T123456789T123456789\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.timesheetCode").value("T123456789T123456789"));
	}
	
	@Test
	void testAddTimesheetCode_TimesheetCode_With_21_Chars() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCode\": \"T123456789T1234567891\", \"timesheetCodeDesc\": \"Test\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/timesheetCodes")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_TIMESHEET_CODE_MSG));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateTimesheetCode() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCodeDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheetCodes/ZZ01")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.timesheetCodeDesc").value("Testing"));
	}
	
	@Test
	void testUpdateTimesheetCode_Unauthorized() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCodeDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheetCodes/ZZ01")
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateTimesheetCode_TimesheetCode_Does_Not_Exist() throws Exception {
		
		String timesheetCodeReq = "{\"timesheetCodeDesc\": \"Testing\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/timesheetCodes/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(timesheetCodeReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeDoesNotExistMsg("Unknown")));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testDeleteTimesheetCode() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheetCodes/ZZ01")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeDeletedMsg("ZZ01")));
	}
	
	@Test
	void testDeleteTimesheetCode_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheetCodes/ZZ01")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteTimesheetCode_TimesheetCode_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/timesheetCodes/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.timesheetCodeDoesNotExistMsg("Unknown")));
	}

}
