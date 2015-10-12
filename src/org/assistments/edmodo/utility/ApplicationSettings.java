package org.assistments.edmodo.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationSettings {
	
	public static final Integer HTTP_ERROR_RESPONE_CODE = 400;
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String edmodo_teacher_constant_str = "TEACHER";
	public static final String edmodo_student_constant_str = "STUDENT";
	public static final String edmodo_email_tail = "@edmodo.com";
	
	/* select * from external_reference_types */
	public static final int external_user_type_id = 1;
	public static final int external_school_type_id = 3;
	public static final int external_student_class_section_type_id = 2;
	public static final int external_class_assignment = 4;
	public static final int external_individual_assignment = 5;
	public static final int external_student_classes = 6;
	public static final int external_access_token = 7;
	
	public static final String edmodo_shchool_ref = "bd03e53e4801837807c5e023ffa816d9"; // this string change everynight in test1.
	
	// Edmodo API
	public static String EdmodoAPIBase = "";
	
	// ASSISTments API
	public static String password = "1234";
	public static final String timeZone = "GMT-4"; 
	public static final String ASSISTmentsPrincipal = "principal";
	public static final String ASSISTmentsProxy = "proxy";
	public static final String ASSISTmentsCourseNunmber = "1";
	public static final String ASSISTmentsSectionNunmber = "1";
	public static final String partner_id = "3";
	public static final String partner_reference = "Hien-Ref";
	public static final String registration_code = "HIEN-API";
	public static final String ASSITments_Auth_WOBehalf = "partner=" + "\"" + partner_reference + "\"";
	
	public static String ASSITments_Base_API = "";
	public static String ASSITments_API_Helper = "";
	public static String ASSITments_Login_WebAPi = "";
	public static String ASSISTments_Login_Teacher = "";
	public static String ASSISTments_Login_Student = "";
	public static String ASSISTments_Login_Failed = "";
	public static String Connector_URL;
	public static String url;
	public static String dbuser;
	public static String dbpassword;
	public static String ASSISTments_Fetch_Score;
	
	private static Properties  prop = new Properties();
	
	static {
//		String configFile = "localhost.config.properties"; //for deployment on localhost
		String configFile = "test1.config.properties"; //for deployment on test1
//		String configFile = "config.properties"; //for deployment on production
//		String configFile = "as16.config.properties";
		LoadProperties(ApplicationSettings.class.getResourceAsStream(configFile));
	}

	static void LoadProperties(InputStream input) {
		try {

			prop.load(input);
			ASSITments_Base_API = prop.getProperty("ASSITments_Base_API");
			ASSITments_API_Helper = prop.getProperty("ASSITments_API_Helper");
			ASSITments_Login_WebAPi = prop.getProperty("ASSITments_Login_WebAPi");
			ASSISTments_Login_Teacher = prop.getProperty("ASSISTments_Login_Teacher");
			ASSISTments_Login_Student = prop.getProperty("ASSISTments_Login_Student");
			ASSISTments_Login_Failed = prop.getProperty("ASSISTments_Login_Failed");
			EdmodoAPIBase = prop.getProperty("Edmodo_API_Base");
			Connector_URL = prop.getProperty("Connector_URL");
			url = prop.getProperty("url");
			dbuser = prop.getProperty("username");
			dbpassword = prop.getProperty("password");
			ASSISTments_Fetch_Score = prop.getProperty("ASSISTments_Fetch_Score");

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException ioE) {
			ioE.printStackTrace();
		}
	}
	
	
	/*
	 * Error handling 
	 *
	 */
	private static Integer errorStatusCode=0;
	public static Integer getErrorStatusCode() {
		return errorStatusCode;
	}

	public static void setErrorStatusCode(Integer errorStatusCode) {
		ApplicationSettings.errorStatusCode = errorStatusCode;
	}


	private static String errorTitle = "";
	private static String errorDetail = "";
	
	public static String getErrorTitle() {
		return errorTitle;
	}

	public static void setErrorTitle(String errorTitle) {
		ApplicationSettings.errorTitle = errorTitle;
	}

	public static String getErrorDetail() {
		return errorDetail;
	}

	public static void setErrorDetail(String errorDetail) {
		ApplicationSettings.errorDetail = errorDetail;
	}

	public static String edmodo_assignment_url_title; //inURL
	public static String edmodo_assignment_title; //inside json request
	public static String edmodo_assignment_description = "An+assingment+from+"
			+ "ASSISTments+app.+Click+on+Launch+Edmodo+App+to+do+the+assignment.";
	/*
	 * map the appId with api_key 
	 */
	public static String getAppApiKey(String appId) {
		String api_key = new String();
		api_key = prop.getProperty(appId);
		edmodo_assignment_url_title = prop.getProperty(appId+"_url_title");
		edmodo_assignment_title = prop.getProperty(appId+"_title");
		/*
		if (appId.equals("73028")) {
			api_key = "6394d0cc748116b42de5dfea25d4b36fc10d3024"; //key on test1
			//api_key = "48f6a7296044e983e1bce7d410b723b72bfe2c62"; //key on production
			edmodo_assignment_url_title="Numeral+to+words";
			edmodo_assignment_title="Numeral to words 2.NBT.A.3";
		}
		else if (appId.equals("37824")) {
			api_key = "80460fcdfe291131bfbf68f8e0e2e585fbf7cca8";
			edmodo_assignment_url_title="Elapsed+Time";
			edmodo_assignment_title="Elapsed Time 3.MD.A.1";
		}
		else if (appId.equals("38729")) {
			api_key = "c4d68ad5e836437f16668d7adcd718c6eac29658"; //key on test1
			//api_key = "7424cccee5f4ae29337158b505305f6b85ebefb4"; //key on production
			edmodo_assignment_url_title="Adding+Mixed+Numbers+-+Like+Denominators";
			edmodo_assignment_title="Adding Mixed Numbers - Like Denominators 4.NF.B.3c";
		}*/
		
		return api_key;
	}
	
	public static String edmodo_turnin_assingment_text = "Here+is+my+assignment+submission";
}

