package com.jtrack.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.jtrack.model.LoginHist;

public interface LoginHistRepository extends JpaRepository <LoginHist, String> {

}
