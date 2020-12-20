package com.jtrack.validation;

public class ValidationUtils {
	
	public static final String VALID_ID_PATTERN 			= "^[a-zA-Z][a-zA-Z0-9_\\-\s]{0,18}[a-zA-Z0-9]$";
	public static final String VALID_USER_PATTERN 			= "^[a-zA-Z][a-zA-Z0-9_\\-]{0,18}[a-zA-Z0-9]$";
	public static final String VALID_PASSWORD_PATTERN 		= "^[a-zA-Z][a-zA-Z0-9_\\-#@\\$]{3,7}$";
	
	private static final String NOTNULL_MSG 				= "must not be NULL";
	private static final String INVALID_MSG 				= "must contain 2 to 20 characters from LETTERS, NUMBERS, UNDERSCORE, MINUS SIGN or SPACE, the first character must be a LETTER, the last character must be a LETTER or a NUMBER";
	
	public static final String NOTNULL_JOB_MSG 				= "Job Priority " + NOTNULL_MSG;
	public static final String INVALID_JOB_MSG 				= "Job Priority " + INVALID_MSG;
	
	public static final String NOTNULL_JOB_PRIORITY_MSG 	= "Job Priority " + NOTNULL_MSG;
	public static final String INVALID_JOB_PRIORITY_MSG 	= "Job Priority " + INVALID_MSG;
	
	public static final String NOTNULL_JOB_RESOLUTION_MSG 	= "Job Resolution " + NOTNULL_MSG;
	public static final String INVALID_JOB_RESOLUTION_MSG 	= "Job Resolution " + INVALID_MSG;
	
	public static final String NOTNULL_JOB_STAGE_MSG 		= "Job Stage " + NOTNULL_MSG;
	public static final String INVALID_JOB_STAGE_MSG 		= "Job Stage " + INVALID_MSG;
	
	public static final String NOTNULL_JOB_STATUS_MSG 		= "Job Status " + NOTNULL_MSG;
	public static final String INVALID_JOB_STATUS_MSG 		= "Job Status " + INVALID_MSG;

	public static final String NOTNULL_JOB_TYPE_MSG 		= "Job Type " + NOTNULL_MSG;
	public static final String INVALID_JOB_TYPE_MSG 		= "Job Type " + INVALID_MSG;

	public static final String NOTNULL_TIMESHEET_CODE_MSG 	= "Timesheet Code " + NOTNULL_MSG;
	public static final String INVALID_TIMESHEET_CODE_MSG 	= "Timesheet Code " + INVALID_MSG;
	
	public static final String NOTBLANK_TIMESHEET_MSG 		= "Timesheet Key Fields ( User, Job, Worked Date ) must not be NULL or BLANK";
	
	public static final String NOTNULL_USER_MSG 			= "User " + NOTNULL_MSG;
	public static final String INVALID_USER_MSG 			= "User must contain 2 to 20 characters from LETTERS, NUMBERS, UNDERSCORE or MINUS SIGN, the first character must be a LETTER, the last character must be a LETTER or a NUMBER";
	public static final String NOTNULL_PASSWORD_MSG 		= "Password " + NOTNULL_MSG;
	public static final String INVALID_PASSWORD_MSG 		= "Password must contain 4 to 10 characters from LETTERS, NUMBERS, UNDERSCORE or MINUS, NUMBER, AT and DOLLAR SIGNS and, the first character must be a LETTER";
	
	
	public static String jobExistsMsg(String id) {
		return existsMsg("Job", id);
	}
	
	public static String jobDoesNotExistMsg(long id) {
		return doesNotExistMsg("Job", "" + id);
	}
	
	public static String jobDeletedMsg(long id) {
		return deletedMsg("Job", "" + id);
	}
	
	public static String childJobExistsForJobMsg(long id) {
		return existsForJobMsg("Child Job", id);
	}
	
	public static String timesheetExistsForJobMsg(long id) {
		return existsForJobMsg("Timesheet", id);
	}
	
	
	public static String jobPriorityExistsMsg(String id) {
		return existsMsg("Job Priority", id);
	}
	
	public static String jobPriorityDoesNotExistMsg(String id) {
		return doesNotExistMsg("Job Priority", id);
	}
	
	public static String jobPriorityDeletedMsg(String id) {
		return deletedMsg("Job Priority", id);
	}
	
	
	public static String jobResolutionExistsMsg(String id) {
		return existsMsg("Job Resolution", id);
	}
	
	public static String jobResolutionDoesNotExistMsg(String id) {
		return doesNotExistMsg("Job Resolution", id);
	}
	
	public static String jobResolutionDeletedMsg(String id) {
		return deletedMsg("Job Resolution", id);
	}
	
	public static String jobStageExistsMsg(String id) {
		return existsMsg("Job Stage", id);
	}
	
	public static String jobStageDoesNotExistMsg(String id) {
		return doesNotExistMsg("Job Stage", id);
	}
	
	public static String jobStageDeletedMsg(String id) {
		return deletedMsg("Job Stage", id);
	}
	
	
	public static String jobStatusExistsMsg(String id) {
		return existsMsg("Job Status", id);
	}
	
	public static String jobStatusDoesNotExistMsg(String id) {
		return doesNotExistMsg("Job Status", id);
	}
	
	public static String jobStatusDeletedMsg(String id) {
		return deletedMsg("Job Status", id);
	}
	
	
	public static String jobTypeExistsMsg(String id) {
		return existsMsg("Job Type", id);
	}
	
	public static String jobTypeDoesNotExistMsg(String id) {
		return doesNotExistMsg("Job Type", id);
	}
	
	public static String jobTypeDeletedMsg(String id) {
		return deletedMsg("Job Type", id);
	}
	
	
	public static String timesheetCodeExistsMsg(String id) {
		return existsMsg("Timesheet Code", id);
	}
	
	public static String timesheetCodeDoesNotExistMsg(String id) {
		return doesNotExistMsg("Timesheet Code", id);
	}
	
	public static String timesheetCodeDeletedMsg(String id) {
		return deletedMsg("Timesheet Code", id);
	}
	
	
	public static String timesheetExistsMsg(String id) {
		return existsMsg("Timesheet", id);
	}
	
	public static String timesheetDoesNotExistMsg(String id) {
		return doesNotExistMsg("Timesheet", id);
	}
	
	public static String timesheetDeletedMsg(String id) {
		return deletedMsg("Timesheet", id);
	}
	
	
	public static String userExistsMsg(String id) {
		return existsMsg("User", id);
	}
	
	public static String userDoesNotExistMsg(String id) {
		return doesNotExistMsg("User", id);
	}
	
	public static String userDeletedMsg(String id) {
		return deletedMsg("User", id);
	}
	
	
	private static String existsMsg(String prefix, String id) {
		return prefix + " ( " + id + " ) already exists";
	}
	
	private static String doesNotExistMsg(String prefix, String id) {
		return prefix + " ( " + id + " ) does not exist";
	}
	
	private static String deletedMsg(String prefix, String id) {
		return prefix + " ( " + id + " ) has been deleted";
	}
	
	private static String existsForJobMsg(String prefix, long id) {
		return prefix + " exists for Job ( " + id + " )";
	}
}
