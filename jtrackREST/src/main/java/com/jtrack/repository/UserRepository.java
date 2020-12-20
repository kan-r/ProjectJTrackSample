package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.jtrack.model.User;

public interface UserRepository extends JpaRepository <User, String> {
	
	@Query(value = "select crypt(:pword, gen_salt('bf'))", nativeQuery = true)
	public String crypt(@Param("pword") String pword);
}
