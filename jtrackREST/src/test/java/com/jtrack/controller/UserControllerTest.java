package com.jtrack.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.jtrack.dto.DeleteResp;
import com.jtrack.dto.UserReq;
import com.jtrack.dto.UserReqUpd;
import com.jtrack.dto.UserResp;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.service.UserService;
import com.jtrack.validation.ValidationUtils;

@ActiveProfiles("test")
@WebMvcTest(UserController.class)
class UserControllerTest {
	
	@Autowired
	private MockMvc mvc;
	
	@MockBean
	private UserService userService;
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetUserList() throws Exception {
		
		List<UserResp> userList = new ArrayList<UserResp>();
		userList.add(new UserResp("ADMIN", "admin10", "Admin", "", true));
		userList.add(new UserResp("KAN", "kan10", "Kan", "Ranganathan", true));
		
		when(userService.getUserList()).thenReturn(userList);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", Matchers.hasSize(2)))
				.andExpect(jsonPath("$[1].userId").value("KAN"));
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testGetUser() throws Exception {
		
		String id = "KAN";
		UserResp user = new UserResp("KAN", "kan10", "Kan", "Ranganathan", true);
		
		when(userService.getUser(id)).thenReturn(user);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.userId").value(id));
	}
	
	@Test
	void testGetUser_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(userService.getUser(id)).thenReturn(null);
		
		mvc.perform(MockMvcRequestBuilders
				.get("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist());
	}

	//---------------------------------------------------------------------------------------

	@Test
	void testAddUser() throws Exception {
		
		String userReq = "{\"userId\":\"TesT-1_2\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		UserReq userReq2 = new UserReq();
		userReq2.setUserId("TesT-1_2");
		userReq2.setPword("test10");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Ranganathan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp("TesT-1_2", "test10", "Kan", "Ranganathan", true);
		
		when(userService.addUser(userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("TesT-1_2"));
	}
	
	@Test
	void testAddUser_User_Exists() throws Exception {
		
		String id = "KAN";
		String userReq = "{\"userId\":\"KAN\", \"pword\": \"kan10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		UserReq userReq2 = new UserReq();
		userReq2.setUserId("KAN");
		userReq2.setPword("kan10");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Ranganathan");
		userReq2.setActive(true);
		
		when(userService.addUser(userReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.userExistsMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.userExistsMsg(id)));
	}
	
	@Test
	void testAddUser_User_Null() throws Exception {
		
		String userReq = "{\"userId\": null, \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
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
		
		UserReq userReq2 = new UserReq();
		userReq2.setUserId("T1");
		userReq2.setPword("test10");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Ranganathan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp("T1", "test10", "Kan", "Ranganathan", true);
		
		when(userService.addUser(userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("T1"));
	}
	
	@Test
	void testAddUser_User_With_20_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"T123456789T123456789\", \"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		UserReq userReq2 = new UserReq();
		userReq2.setUserId("T123456789T123456789");
		userReq2.setPword("test10");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Ranganathan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp("T123456789T123456789", "test10", "Kan", "Ranganathan", true);
		
		when(userService.addUser(userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
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
		
		UserReq userReq2 = new UserReq();
		userReq2.setUserId("TEST");
		userReq2.setPword("test");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Ranganathan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp("TEST", "test", "Kan", "Ranganathan", true);
		
		when(userService.addUser(userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.userId").value("TEST"));
	}
	
	@Test
	void testAddUser_Password_With_8_Chars() throws Exception {
		
		String userReq = "{\"userId\":\"TEST\", \"pword\": \"Te$-1_#@\", \"firstName\": \"Kan\", \"lastName\": \"Ranganathan\", \"active\": true}";
		
		UserReq userReq2 = new UserReq();
		userReq2.setUserId("TEST");
		userReq2.setPword("Te$-1_#@");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Ranganathan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp("TEST", "Te$-1_#@", "Kan", "Ranganathan", true);
		
		when(userService.addUser(userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.post("/users")
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
		
		String id = "KAN";
		String userReq = "{\"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		UserReqUpd userReq2 = new UserReqUpd();
		userReq2.setPword("test10");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Rangan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp(id, "test10", "Kan", "Rangan", true);
		
		when(userService.updateUser(id, userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/{id}", id)
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.lastName").value("Rangan"));
	}
	
	@Test
	void testUpdateUser_User_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		String userReq = "{\"pword\": \"test10\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		UserReqUpd userReq2 = new UserReqUpd();
		userReq2.setPword("test10");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Rangan");
		userReq2.setActive(true);
		
		when(userService.updateUser(id, userReq2)).thenThrow(
				new InvalidDataException(
						ValidationUtils.userDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/{id}", id)
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.userDoesNotExistMsg(id)));
	}
	
	@Test
	void testUpdateUser_Password_Null() throws Exception {
		
		String userReq = "{\"pword\": null, \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/KAN")
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
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.error").value("invalid_data"))
				.andExpect(jsonPath("$.message").value(ValidationUtils.INVALID_PASSWORD_MSG));
	}
	
	@Test
	void testUpdateUser_Password_With_4_Chars() throws Exception {
		
		String id = "KAN";
		String userReq = "{\"pword\": \"test\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		UserReqUpd userReq2 = new UserReqUpd();
		userReq2.setPword("test");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Rangan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp(id, "test", "Kan", "Rangan", true);
		
		when(userService.updateUser(id, userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/{id}", id)
				.content(userReq)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.pword").value("test"));
	}
	
	@Test
	void testUpdateUser_Password_With_8_Chars() throws Exception {
		
		String id = "KAN";
		String userReq = "{\"pword\": \"Te$-1_#@\", \"firstName\": \"Kan\", \"lastName\": \"Rangan\", \"active\": true}";
		
		UserReqUpd userReq2 = new UserReqUpd();
		userReq2.setPword("Te$-1_#@");
		userReq2.setFirstName("Kan");
		userReq2.setLastName("Rangan");
		userReq2.setActive(true);
		
		UserResp UserResp = new UserResp(id, "Te$-1_#@", "Kan", "Rangan", true);
		
		when(userService.updateUser(id, userReq2)).thenReturn(UserResp);
		
		mvc.perform(MockMvcRequestBuilders
				.put("/users/{id}", id)
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
		
		String id = "KAN";
		
		when(userService.deleteUser(id)).thenReturn(
				new DeleteResp(ValidationUtils.userDeletedMsg(id))
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.message").value(ValidationUtils.userDeletedMsg(id)));
	}
	
	@Test
	void testDeleteUser_User_Does_Not_Exist() throws Exception {
		
		String id = "Unknown";
		
		when(userService.deleteUser(id)).thenThrow(
				new InvalidDataException(
						ValidationUtils.userDoesNotExistMsg(id)
				)
		);
		
		mvc.perform(MockMvcRequestBuilders
				.delete("/users/{id}", id)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("$.message").value(ValidationUtils.userDoesNotExistMsg(id)));
	}

}
