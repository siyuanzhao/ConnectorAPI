package org.assistments.service.controller;

import org.assistments.service.domain.User;

/**
 * This interface represents User Management in ASSISTments Service
 * @author szhao
 *
 */
public interface AccountController {
	
	/**
	 * Create a new user account
	 * @param user -- {@link User} contains user information
	 * @return identifier for this user from ASSISTments
	 * @throws Exception -- if something goes wrong
	 */
	public String createUser(User user)
			throws Exception;
	
	/**
	 * Create access token for the user
	 * @param userRef -- identifier for the user
	 * @return access token for this user
	 * @throws Exception
	 */
	public String createAccessToken(String userRef)
		throws Exception;
	
	/**
	 * Check if there is an account associated with the email
	 * @param email -- email address
	 * @return true -- if the account exists
	 * @return false -- if the account doesn't exist
	 * @throws Exception
	 */
	public boolean isEmailInUse(String email) 
			throws Exception;
	
	public User getUserProfile(String id) throws Exception;
	
	public User getUserProfileById(long id);

	public boolean isTeacherExist(String email);

}
