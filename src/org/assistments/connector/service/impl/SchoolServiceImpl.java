package org.assistments.connector.service.impl;

import org.assistments.connector.service.SchoolService;
import org.assistments.service.controller.ErrorLogController;
import org.assistments.service.controller.SchoolController;
import org.assistments.service.controller.impl.SchoolControllerWebImpl;

public class SchoolServiceImpl implements SchoolService {
	
	String apiPartnerRef;
	String accessToken;
	SchoolController sc;
	String errorSourceType = "Connector";
	
	
	public SchoolServiceImpl(String apiPartnerRef, String accessToken) {
		this.apiPartnerRef = apiPartnerRef;
		this.accessToken = accessToken;
		sc = new SchoolControllerWebImpl(apiPartnerRef, accessToken);
	}

	@Override
	public boolean assignUserToSchool(String userRef, String schoolRef) {
		boolean b = false;
		
		try {
			b = sc.assignUserToSchool(userRef, schoolRef);
		} catch (Exception e) {
			// TODO userId should be something else rather than 1
			ErrorLogController.addNewError(e, errorSourceType);
			e.printStackTrace();
			throw new RuntimeException(e);
		}
		
		return b;
	}

}
