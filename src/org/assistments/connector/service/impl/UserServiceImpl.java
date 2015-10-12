package org.assistments.connector.service.impl;

import org.assistments.connector.service.UserService;
import org.assistments.service.controller.ErrorLogController;
import org.assistments.service.controller.UserController;
import org.assistments.service.controller.impl.UserControllerWebImpl;
import org.assistments.service.domain.User;

public class UserServiceImpl implements UserService {

	String apiPartnerRef;
	String accessToken;
	UserController uc;
	String errorSourceType = "Connector";
	
	public UserServiceImpl(String apiPartnerRef, String accessToken) {
		this.apiPartnerRef = apiPartnerRef;
		uc = new UserControllerWebImpl(apiPartnerRef, accessToken);
	}
	
	@Override
	public User getUserProfile(String externalRef) {
		User user = new User();
		try {
			user = uc.getUserProfile(externalRef);
		} catch (Exception e) {
			// TODO userId should be something else rather than 1
			ErrorLogController.addNewError(e, errorSourceType);
			throw new RuntimeException(e);
		}
		
		return user;
	}	
}
