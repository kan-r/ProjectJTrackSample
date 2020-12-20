package com.jtrack.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDate;
import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import com.jtrack.exception.InvalidDataException;

class GenUtilsTest {

	@Test
	void testToBoolean() {
		assertThat(GenUtils.toBoolean("true")).isEqualTo(true);
		assertThat(GenUtils.toBoolean("True")).isEqualTo(true);
		assertThat(GenUtils.toBoolean("TRUE")).isEqualTo(true);
		
		assertThat(GenUtils.toBoolean("false")).isEqualTo(false);
		assertThat(GenUtils.toBoolean("False")).isEqualTo(false);
		assertThat(GenUtils.toBoolean("FALSE")).isEqualTo(false);
		
		assertThat(GenUtils.toBoolean("yes")).isEqualTo(false);
		assertThat(GenUtils.toBoolean("no")).isEqualTo(false);
		
		assertThat(GenUtils.toBoolean("1")).isEqualTo(false);
		assertThat(GenUtils.toBoolean("0")).isEqualTo(false);
		
		assertThat(GenUtils.toBoolean("")).isEqualTo(false);
		assertThat(GenUtils.toBoolean(null)).isEqualTo(false);
	}

	@Test
	void testFormatDate() {
		
		LocalDate localDate = LocalDate.of(2020, 12, 15);
		
		// valid
		assertThat(GenUtils.formatDate(localDate, "dd/MM/yyyy")).isEqualTo("15/12/2020");
		assertThat(GenUtils.formatDate(localDate, "yyyy-MM-dd")).isEqualTo("2020-12-15");
		assertThat(GenUtils.formatDate(localDate, "E, dd MMM yyyy")).isEqualTo("Tue, 15 Dec 2020");
		
		// default
		assertThat(GenUtils.formatDate(localDate, null)).isEqualTo("2020-12-15");
		assertThat(GenUtils.formatDate(localDate, "")).isEqualTo("2020-12-15");
		
		// invalid
		assertThat(GenUtils.formatDate(localDate, "bb/CC/yyyy")).isEqualTo("");
		assertThat(GenUtils.formatDate(null, "dd/MM/yyyy")).isEqualTo("");
		
	}

	@Test
	void testFormatDateTime() {
		
		LocalDateTime localDateTime = LocalDateTime.of(2020, 12, 15, 13, 30, 20);
		
		// valid
		assertThat(GenUtils.formatDateTime(localDateTime, "dd/MM/yyyy hh:mm:ss a")).isEqualTo("15/12/2020 01:30:20 pm");
		assertThat(GenUtils.formatDateTime(localDateTime, "yyyy-MM-dd HH:mm:ss")).isEqualTo("2020-12-15 13:30:20");
		assertThat(GenUtils.formatDateTime(localDateTime, "E, dd MMM yyyy")).isEqualTo("Tue, 15 Dec 2020");
		
		// default
		assertThat(GenUtils.formatDateTime(localDateTime, null)).isEqualTo("2020-12-15T13:30:20");
		assertThat(GenUtils.formatDateTime(localDateTime, "")).isEqualTo("2020-12-15T13:30:20");
		
		// invalid
		assertThat(GenUtils.formatDateTime(localDateTime, "bb/CC/yyyy hh:mm:ss a")).isEqualTo("");
		assertThat(GenUtils.formatDateTime(null, "dd/MM/yyyy")).isEqualTo("");
	}

	@Test
	void testToDateString() {
		// valid ( format yyyy-MM-dd )
		try {
			LocalDate date = GenUtils.toDate("2020-10-25");
			
			assertThat(date.getYear()).isEqualTo(2020);
			assertThat(date.getMonthValue()).isEqualTo(10);
			assertThat(date.getDayOfMonth()).isEqualTo(25);
			
			assertNull(GenUtils.toDate(null));
			assertNull(GenUtils.toDate(""));
			
		} catch (InvalidDataException e) {
			// this shouldn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
		
		// invalid
		assertThrows(InvalidDataException.class, () -> {
			GenUtils.toDate("25-10-2020");
	    });
		
		// invalid
		assertThrows(InvalidDataException.class, () -> {
			GenUtils.toDate("2020-10-25 13:30");
	    });
	}

	@Test
	void testToDateStringString() {
		String patt = "dd/MM/yyyy";
		
		// valid
		try {
			LocalDate date = GenUtils.toDate("25/10/2020", patt);
			
			assertThat(date.getYear()).isEqualTo(2020);
			assertThat(date.getMonthValue()).isEqualTo(10);
			assertThat(date.getDayOfMonth()).isEqualTo(25);
			
			assertNull(GenUtils.toDate(null, patt));
			assertNull(GenUtils.toDate("", patt));
			
		} catch (InvalidDataException e) {
			// this shouldn't happen
			fail("Unexpected exception: " + e.getMessage());
		}
		
		// invalid
		assertThrows(InvalidDataException.class, () -> {
			GenUtils.toDate("25-10-2020", patt);
	    });
		
		// invalid
		assertThrows(InvalidDataException.class, () -> {
			GenUtils.toDate("2020-10-25 13:30", patt);
	    });
	}
	
}
