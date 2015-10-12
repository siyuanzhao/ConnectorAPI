package org.assistments.connector.service;

import org.assistments.service.domain.User;

@Deprecated
public interface UserService {
	
	public User getUserProfile(String externalRef);
	
}
