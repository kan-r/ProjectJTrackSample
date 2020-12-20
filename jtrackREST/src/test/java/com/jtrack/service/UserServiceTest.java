package com.jtrack.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import com.jtrack.dto.UserResp;
import com.jtrack.dto.UserReq;
import com.jtrack.dto.UserReqUpd;
import com.jtrack.exception.InvalidDataException;
import com.jtrack.mapper.UserMapper;
import com.jtrack.model.User;
import com.jtrack.validation.ValidationUtils;

// testing using in memory (H2) database
@DataJpaTest
class UserServiceTest {
	
	@Autowired
	private UserService userService;
	
	@Autowired
    private TestEntityManager testEntityManager;
	
	@TestConfiguration
    static class UserServiceConfiguration {
		
		@Bean
		public UserService userService() {
			return new UserServiceImpl();
		}
		
		@Bean 
		public UserMapper userMapper() {
			return new UserMapper();
		}
    }
	
	@BeforeEach
	void setUp() throws Exception {
		
		testEntityManager.persist(new User("ADMIN", "admin10", "Admin", "", true, null, null, null, null));
		testEntityManager.persist(new User("KAN", "kan10", "Kan", "Ranganathan", true, null, "ADMIN", null, null));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetUserList() {
		assertThat(userService.getUserList()).hasSize(2);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testGetUser() {
		
		String id = "KAN";
		assertThat(userService.getUser(id).getUserId()).isEqualTo(id);
	}
	
	@Test
	void testGetUser_Does_Not_Exist() {
		
		String id = "Unknown";
		assertThat(userService.getUser(id)).isEqualTo(null);
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testAddUser() {
		
		String id = "Test";
		
		UserReq userReq = new UserReq();
		userReq.setUserId(id);
		userReq.setPword("test10");
		userReq.setFirstName("Kan");
		userReq.setLastName("Ranganathan");
		userReq.setActive(true);
		
		try {
			assertThat(userService.addUser(userReq).getUserId()).isEqualTo(id);
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testAddUser_User_Exists() {
		
		String id = "KAN";
		
		UserReq userReq = new UserReq();
		userReq.setUserId(id);
		userReq.setPword("kan10");
		userReq.setFirstName("Kan");
		userReq.setLastName("Ranganathan");
		userReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			userService.addUser(userReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.userExistsMsg(id));
	}
	
	//---------------------------------------------------------------------------------------

	@Test
	void testUpdateUser() {
		
		String id = "KAN";
		
		UserReqUpd userReq = new UserReqUpd();
		userReq.setPword("test10");
		userReq.setFirstName("Kan");
		userReq.setLastName("Rangan");
		userReq.setActive(true);
		
		try {
			UserResp userResp = userService.updateUser(id, userReq);
			
			assertThat(userResp.getUserId()).isEqualTo(id);
			assertThat(userResp.getLastName()).isEqualTo("Rangan");
			
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testUpdateUser_User_Does_Not_Exist() {
		
		String id = "Unknown";

		UserReqUpd userReq = new UserReqUpd();
		userReq.setPword("test10");
		userReq.setFirstName("Kan");
		userReq.setLastName("Rangan");
		userReq.setActive(true);
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			userService.updateUser(id, userReq);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.userDoesNotExistMsg(id));
	}
	
	//---------------------------------------------------------------------------------------
	
	@Test
	void testDeleteUser() {
		
		String id = "KAN";
		
		try {
			assertThat(userService.deleteUser(id).getMessage()).isEqualTo(ValidationUtils.userDeletedMsg(id));
		} catch (InvalidDataException e) {
			// this shoudn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
	}
	
	@Test
	void testDeleteUser_User_Does_Not_Exist() {
		
		String id = "Unknown";
		
		Exception exception = assertThrows(InvalidDataException.class, () -> {
			userService.deleteUser(id);
	    });
		
		assertThat(exception.getMessage()).isEqualTo(ValidationUtils.userDoesNotExistMsg(id));
	}

}
