package com.jtrack.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import com.jtrack.exception.InvalidDataException;

public class GenUtils {
	
	public static boolean toBoolean(String val) {
		return Boolean.parseBoolean(val);
	}
	
	public static String formatDate(LocalDate localDate, String formatPattern) {
		if(localDate == null) {
			return "";
		}
		
		DateTimeFormatter dtTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		
		if(formatPattern == null || formatPattern.isBlank()) {
			return dtTimeFormatter.format(localDate);
		}
		
		try {
			dtTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
			return dtTimeFormatter.format(localDate);
		}catch(IllegalArgumentException e) {
			return "";
		}
	}

	public static String formatDateTime(LocalDateTime localDateTime, String formatPattern) {
		if(localDateTime == null) {
			return "";
		}
		
		DateTimeFormatter dtTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
		
		if(formatPattern == null || formatPattern.isBlank()) {
			return dtTimeFormatter.format(localDateTime);
		}
		
		try {
			dtTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
			return dtTimeFormatter.format(localDateTime);
		}catch(IllegalArgumentException e) {
			return "";
		}
	}
	
	// valid date format yyyy-MM-dd
	public static LocalDate toDate(String dateStr) throws InvalidDataException {
		return toDate(dateStr, "yyyy-MM-dd");
	}
	
	public static LocalDate toDate(String dateStr, String formatPattern) throws InvalidDataException {
		if(dateStr == null || dateStr.isBlank()) {
			return null;
		}
		
		DateTimeFormatter dtTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
		
		if(formatPattern != null && !formatPattern.isBlank()) {
			dtTimeFormatter = DateTimeFormatter.ofPattern(formatPattern);
		}
		
		try{
			return LocalDate.parse(dateStr, dtTimeFormatter);
		}catch(DateTimeParseException e) {
			throw new InvalidDataException(e.getMessage() + ", expected date format " + formatPattern);
		}
	}
	
	public static String getClientIpAddress() {
		String ipAddr = "";
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if(authentication != null) {
			Object details = authentication.getDetails();
			if(details instanceof WebAuthenticationDetails) {
				ipAddr = ((WebAuthenticationDetails)details).getRemoteAddress();
			}
		}
		
		return ipAddr;
	}
	
	public static String getCurrentUserId() {
		String user = null;
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		
		if (authentication != null && !(authentication instanceof AnonymousAuthenticationToken)) {
			user = authentication.getName();
			if(user != null) {
				user =  user.toUpperCase();
			}
		}
		
		return user;
	}
}
