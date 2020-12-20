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

import com.jtrack.model.User;
import com.jtrack.util.TokenUtils;
import com.jtrack.validation.ValidationUtils;

@SpringBootTest
@AutoConfigureMockMvc
//to use test (in memory H2) database
@AutoConfigureTestDatabase
@AutoConfigureTestEntityManager
@Transactional
class UserControllerIntegrationTest {
	
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
		
		testEntityManager.persist(new User("ADMIN", "admin10", "Admin", "", true, null, null, null, null));
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "ADMIN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetUserList() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[1].userId").value("KAN"));
	}
	
	@Test
	void testGetUserList_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetUser() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value("KAN"));
	}
	
	@Test
	void testGetUser_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users/KAN")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testGetUser_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddUser() throws Exception {
		
		String userReq = "{\"userId\":\"TesT-1_2\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("TesT-1_2"));
	}
	
	@Test
	void testAddUser_Forbidden() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	@Test
	void testAddUser_Unauthorized() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testAddUser_User_Exists() throws Exception {
		
		String userReq = "{\"userId\":\"KAN\", \"pword\": \"kan10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.userExistsMsg("KAN")));
	}
	
	@Test
	void testAddUser_User_Null() throws Exception {
		
		String userReq = "{\"userId\": null, \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_USER_MSG));
	}
	
	@Test
	void testAddUser_User_Empty() throws Exception {
		
		String userReq = "{\"userId\":\"\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_User_Blank() throws Exception {
		
		String userReq = "{\"userId\":\" \", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_User_With_Space() throws Exception {
		
		String userReq = "{\"userId\":\"T EST\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_User_Start_With_Invalid_Char() throws Exception {
		
		String userReq = "{\"userId\":\"123\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_User_End_With_Invalid_Char() throws Exception {
		
		String userReq = "{\"userId\":\"T1@\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_User_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String userReq = "{\"userId\":\"TEST@10\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_User_With_1_Char() throws Exception {
		
		String userReq = "{\"userId\":\"T\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_User_With_2_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"T1\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("T1"));
	}
	
	@Test
	void testAddUser_User_With_20_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"T123456789T123456789\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("T123456789T123456789"));
	}
	
	@Test
	void testAddUser_User_With_21_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"T123456789T1234567891\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_USER_MSG));
	}
	
	@Test
	void testAddUser_Password_Null() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": null, \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_Empty() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_Blank() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \" \", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_With_Space() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"t est10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_Start_With_Invalid_Char() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"1234\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_End_With_Invalid_Char() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"test10!\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"test!10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_With_3_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"tes\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testAddUser_Password_With_4_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"test\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("TEST"));
	}
	
	@Test
	void testAddUser_Password_With_8_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"Te$-1_#@\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("TEST"));
	}
	
	@Test
	void testAddUser_Password_With_9_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"test10101\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testUpdateUser() throws Exception {
		
		String userReq = "{\"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName").value("Rangan"));
	}
	
	@Test
	void testUpdateUser_Forbidden() throws Exception {
		
		String userReq = "{\"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	@Test
	void testUpdateUser_Unauthorized() throws Exception {
		
		String userReq = "{\"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testUpdateUser_User_Does_Not_Exist() throws Exception {
		
		String userReq = "{\"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.userDoesNotExistMsg("Unknown")));
	}
	
	@Test
	void testUpdateUser_Password_Null() throws Exception {
		
		String userReq = "{\"pword\": null, \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.NOTNULL_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_Empty() throws Exception {
		
		String userReq = "{\"pword\": \"\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_Blank() throws Exception {
		
		String userReq = "{\"pword\": \" \", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_With_Space() throws Exception {
		
		String userReq = "{\"pword\": \"t est10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_Start_With_Invalid_Char() throws Exception {
		
		String userReq = "{\"pword\": \"12345678\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_End_With_Invalid_Char() throws Exception {
		
		String userReq = "{\"pword\": \"test10%\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_With_Invalid_Char_In_The_Middle() throws Exception {
		
		String userReq = "{\"pword\": \"test&10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_With_3_Chars() throws Exception {
		
		String userReq = "{\"pword\": \"tes\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_With_4_Chars() throws Exception {
		
		String userReq = "{\"pword\": \"test\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pword").value("test"));
	}
	
	@Test
	void testUpdateUser_Password_With_8_Chars() throws Exception {
		
		String userReq = "{\"pword\": \"Te$-1_#@\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pword").value("Te$-1_#@"));
	}
	
	@Test
	void testUpdateUser_Password_With_9_Chars() throws Exception {
		
		String userReq = "{\"pword\": \"test10101\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteUser() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.userDeletedMsg("KAN")));
	}
	
	@Test
	void testDeleteUser_Forbidden() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/users/KAN")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "kan", "kan10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isForbidden());
	}
	
	@Test
	void testDeleteUser_Unauthorized() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/users/KAN")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isUnauthorized());
	}
	
	@Test
	void testDeleteUser_User_Does_Not_Exist() throws Exception {
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/users/Unknown")
				.header("Authorization", TokenUtils.obtainAccessToken(mvc, "admin", "admin10"))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.userDoesNotExistMsg("Unknown")));
	}

}
