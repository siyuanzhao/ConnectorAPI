package org.assistments.connector.utility;

public class LocalhostSettings {
//	public static final String ASSISSTments_URL = "http://csta14-5.cs.wpi.edu/";
	public static final String ASSISSTments_URL = "https://test1.assistments.org/";
	public static final String DIRECT_URL= "http://csta14-5.cs.wpi.edu/direct";
	public static final String LOCAL_ASSISTMENTS_URL = "http://localhost:3000/";

	public static final String API_BASE = "http://test1.assistments.org:8080/api2";
//	public static final String API_BASE = "http://localhost:8080/api2";
	public static final String LOGIN_URL = ASSISSTments_URL + "api2_helper/user_login";
	public static final String GET_SCORE_URL = ASSISSTments_URL + "api2_helper/get_score";
	public static final String TEACHER_PAGE = ASSISSTments_URL + "external_teacher/student_class/problem_set";
	public static final String STUDENT_PAGE = ASSISSTments_URL + "external_tutor/student_class/problem_set";
	
	public static final String CONNECTOR_URL = "http://hmd12-2.cs.wpi.edu:8080/connector/";
//	public static final String CONNECTOR_HTTPS_URL = "https://csta14-5.cs.wpi.edu:8443/connector/";
	public static final String CONNECTOR_HTTPS_URL = CONNECTOR_URL;
	
	public static final String DATABASE_URL = "jdbc:postgresql://test1.assistments.org/assistment_production";
//	public static final String DATABASE_URL = "jdbc:postgresql://as16.cs.wpi.edu/assistment_production";
	public static final String DATABASE_PASSWORD = "RuePierre1503";
}
