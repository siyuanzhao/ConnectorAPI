package org.assistments.service.controller;

import org.assistments.service.domain.User;

public interface UserController {
	
	public User getUserProfile(String userRef) 
			throws Exception;
}
