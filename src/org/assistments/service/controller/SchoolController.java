package org.assistments.service.controller;

/**
 * This interface represents School Management in ASSISTments Service
 * @author szhao
 *
 */
public interface SchoolController {
	/**
	 * Assign a user to a school
	 * @param userRef -- identifier for the user
	 * @param schoolRef -- identifier to the school
	 * @return true -- if successfully
	 * @return false -- if unsuccessfully 
	 * @throws Exception
	 */
	public boolean assignUserToSchool(String userRef, String schoolRef) 
			throws Exception;
}
