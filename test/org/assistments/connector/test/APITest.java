package org.assistments.connector.test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.exception.TransferUserException;
import org.assistments.connector.service.AccountService;
import org.assistments.connector.service.AssignmentService;
import org.assistments.connector.service.SchoolService;
import org.assistments.connector.service.StudentClassService;
import org.assistments.connector.service.impl.AccountServiceImpl;
import org.assistments.connector.service.impl.AssignmentServiceImpl;
import org.assistments.connector.service.impl.SchoolServiceImpl;
import org.assistments.connector.service.impl.StudentClassServiceImpl;
import org.assistments.connector.utility.Constants;
import org.assistments.service.domain.ReferenceTokenPair;
import org.assistments.service.domain.User;

public class APITest {

	public static final String TIMEZONE = "GMT-4";
	public static final String REGISTRATION_CODE = "LAOLI";

	public static void main(String[] args) {
		String apiPartner = "Direct-Ref";
		String schoolRef = "070c9b2f-0ebe-46f0-a22e-e7d3d8a16ced";
		String problemSetId = "70321";
		User teacher = populateTeacherInfo("first_name", "last_name", "Mr. Teacher");
//		UserService us = new UserServiceImpl(apiPartner);
		AccountService as = new AccountServiceImpl(apiPartner);
		String userRef = new String();
		String accessToken = new String();
		//create teacher account and access token
		String thirdPartyId = "connector api test teacher";
		String stuClassId = "connector api test class";
		String assignmentId = "connector api test assignment";
		String classRef = new String();
	
		ReferenceTokenPair pair;
		try {
			pair = as.transferUser(teacher, schoolRef, thirdPartyId, "test_token", "This is just a test");
			userRef = pair.getExternalRef();
			accessToken = pair.getAccessToken();
		} catch (TransferUserException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		StudentClassService scs = new StudentClassServiceImpl(apiPartner, accessToken);
		classRef = scs.transferStudentClass("Student Class", stuClassId, "test_token", "This is API test");
		AssignmentService assignmentService = new AssignmentServiceImpl(apiPartner, accessToken);
		if(!assignmentService.isExternalAssignmentExists(assignmentId)) {
			//create a problem set
			assignmentService.createClassAssignment(problemSetId, classRef, 
					assignmentId, "test_token", "This is just a test");
		} else {
			try {
				as.find(ColumnNames.PARTNER_EXTERNAL_REFERENCE, assignmentId);
			} catch (ReferenceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//create student account
		String studentId = "connector api test student";
		String studentRef = new String();
//		String studentToken = new String();
		
		if(as.isExternalUserExists(studentId)) {
			List<PartnerToAssistments> student = new ArrayList<PartnerToAssistments>();
			try {
				student = as.find(ColumnNames.PARTNER_EXTERNAL_REFERENCE, studentId);
			} catch (ReferenceNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			studentRef = student.get(0).getAssistmentsExternalRefernce();
		} else {
			User student = populateStudentInfo("first name", "last name", "user name");
			try {
				pair = as.createUser(student, studentId, "test_token", "For test");
//				studentToken = pair.getAccessToken();
				studentRef = pair.getExternalRef();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//enroll student in the class
		scs.enrollStudent(studentRef, classRef);
		System.out.println("Done");
	}

	public static User populateTeacherInfo(String firstName, String lastName,
			String displayName) {
		User user = new User();
		user.setUserType(Constants.PRINCIPAL);
		user.setPassword("12345");
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setDisplayName(displayName);

		Long time = uniqueCurrentTimeMS();
		String fakeEmail = time.toString() + "@als.com";
		user.setEmail(fakeEmail);
		user.setUsername(time.toString());
		user.setTimeZone(TIMEZONE);
		user.setRegistrationCode(REGISTRATION_CODE);
		return user;
	}
	
	public static User populateStudentInfo(String firstName, String lastName, String userName) {
		User user = new User();
		user.setUserType(Constants.PROXY);
		user.setFirstName(firstName);
		user.setLastName(lastName);
		user.setUsername(userName);
		user.setTimeZone(TIMEZONE);
		return user;
	}

	private static final AtomicLong LAST_TIME_MS = new AtomicLong();

	public static long uniqueCurrentTimeMS() {
		long now = System.currentTimeMillis();
		while (true) {
			long lastTime = LAST_TIME_MS.get();
			if (lastTime >= now)
				now = lastTime + 1;
			if (LAST_TIME_MS.compareAndSet(lastTime, now))
				return now;
		}
	}
}
