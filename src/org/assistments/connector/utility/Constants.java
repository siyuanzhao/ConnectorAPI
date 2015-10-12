package org.assistments.connector.utility;

public class Constants extends  LocalhostSettings {		
	public static final String TEACHER_ROLE = "teacher";
	public static final String STUDENT_ROLE = "student";
	
	public static final String PRINCIPAL = "principal";
	public static final String PROXY = "proxy";
	
	public static final String TEACHER_ASSIGNMENT_PAGE = ASSISSTments_URL + "external_teacher/student_class/assignment/";
	
	/* select * from external_reference_types */
	public static final int EXTERNAL_USER_TYPE_ID = 1;
	public static final int EXTERNAL_SCHOOL_TYPE_ID = 3;
	public static final int EXTERNAL_CLASS_SECTION_TYPE_ID = 2;
	public static final int EXTERNAL_ASSIGNMENT_TYPE_ID = 4;
	public static final int EXTERNAL_INDIVIDUAL_ASSIGNMENT_TYPE_ID = 5;
	public static final int EXTERNAL_STUDENT_CLASS_TYPE_ID = 6;
	public static final int SHARE_LINK_TYPE_ID = 7;
	
	public static final String ERROR_SOURCE_TYPE = "Connector";
	public static final int BRAND_NEW_USER = 0;
}
