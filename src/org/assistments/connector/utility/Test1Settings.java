package org.assistments.connector.utility;

public class Test1Settings {
	public static final String ASSISSTments_URL = "https://test1.assistments.org/";

	public static final String DIRECT_URL = "https://test1.assistments.org/direct";
	public static final String API_BASE = "http://test1.assistments.org:8080/api2";
	
	public static final String LOGIN_URL = ASSISSTments_URL + "api2_helper/user_login";
	public static final String GET_SCORE_URL = ASSISSTments_URL + "api2_helper/get_score";
	public static final String TEACHER_PAGE = ASSISSTments_URL + "external_teacher/student_class/problem_set";
	
	public static final String STUDENT_PAGE = ASSISSTments_URL + "external_tutor/student_class/problem_set";
	
//	public static final String CONNECTOR_URL = "https://test1.assistments.org/connector/";
//	public static final String CONNECTOR_HTTPS_URL = "https://test1.assistments.org/connector/";
	
	public static final String CONNECTOR_URL = "http://test1.assistments.org:8080/direct/";
	public static final String CONNECTOR_HTTPS_URL = CONNECTOR_URL;
	
	public static final String DATABASE_URL = "jdbc:postgresql://127.0.0.1/assistment_production";
	public static final String DATABASE_PASSWORD = "RuePierre1503";
	
}
