package org.assistments.connector.service;

/**
 * This interface contains the operations on ASSISTments School
 * @author szhao
 *
 */
public interface SchoolService {
	
	/**
	 * Assign a user to a school
	 * @param userRef -- external reference for the user from ASSISTments
	 * @param schoolRef -- external reference for the school from ASSISTments
	 * @return true -- if successfully
	 * @return false -- if unsuccessfully
	 */
	public boolean assignUserToSchool(String userRef, String schoolRef); 

}
