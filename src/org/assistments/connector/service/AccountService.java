package org.assistments.connector.service;

import java.util.List;

import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.exception.TransferUserException;
import org.assistments.service.domain.ReferenceTokenPair;
import org.assistments.service.domain.User;

/**
 * This interface contains the operations on ASSISTments  user account 
 * and manages the association between ASSISTments user and Partner user.
 * @author szhao
 *
 */
public interface AccountService {
	
	/**
	 * Transfer the user from Partner to ASSISTments.
	 * First it checks if partnerExternalRef already exists. 
	 * If it doesn't, a new user will be created inside ASSISTments, and system will assign this user to a certain school.
	 * Store the association between new ASSISTments user and Partner user into database.
	 * If it does exist, external reference and access token will be fetched from database.
	 * @param user -- It contains information about this user, like user type, first name, last name ...
	 * @param schoolRef -- External reference for a certain school. This comes from ASSISTments.
	 * @param partnerExternalRef -- Partner external reference for this user. This comes from Partner.
	 * @param partnerAccessToken -- Partner access token for this user. This comes from Partner.
	 * @param note -- To keep extra information
	 * @return ReferenceTokenPair object. It contains external reference and access token for this user.
	 * @throws TransferUserException
	 */
	public ReferenceTokenPair transferUser(User user, String schoolRef,
			String partnerExternalRef, String partnerAccessToken, String note) throws TransferUserException;
	
	/**
	 * Create a new user inside ASSISTments.
	 * Store the association between new user and Partner user into database.
	 * @param user -- It contains information about this user, like user type, first name, last name ...
	 * @param partnerExternalRef -- Partner external reference for this user. This comes from Partner.
	 * @param partnerAccessToken -- Partner access token for this user. This comes from Partner.
	 * @param note -- To keep extra information
	 * @return ReferenceTokenPair object. It contains external reference and access token for this user.
	 * @throws Exception
	 */
	public ReferenceTokenPair createUser(User user, String partnerExternalRef,
			String partnerAccessToken, String note) throws Exception;
	
	/**
	 * Only create a new access token for a user.
	 * @param userRef -- External reference for the user. It comes from ASSISTments.
	 * @return access token for this user
	 * @throws Exception -- if this user already has access token, an exception will be thrown out.
	 */
	public String createAccessToken(String userRef) throws Exception;
	
	/**
	 * Check if there is ASSISTments account associated with this email.
	 * @param email -- email address
	 * @return true -- if email already exists
	 * @return false -- if email doesn't exist
	 */
	public boolean isEmailInUse(String email);
	
	/**
	 * Check if Partner user already exists based on partner_external_reference
	 * @param partnerExternalRef -- partner external reference for this user from Partner.
	 * @return true -- if the user already exists.
	 * @return false -- if the user doesn't exist.
	 */
	public boolean isExternalUserExists(String partnerExternalRef);

	/**
	 * Find a certain external user based on column name and value 
	 * @param cn -- column name. It comes from table partner_to_assistments_links (partner_external_references)
	 * @param value -- value for the column
	 * @return a list of external users
	 * @throws ReferenceNotFoundException -- If nothing is found, this exception will be thrown out.
	 */
	public List<PartnerToAssistments> find(PartnerToAssistments.ColumnNames cn, String value)
		throws ReferenceNotFoundException;
	
	/**
	 * Update table for this user based on user external reference outside and inside ASSISTments
	 * I assume external_reference and partner_external_reference shouldn't change over the time. So please be aware that this method is intended to
	 * update assistments_access_token, partner_access_token and note
	 * @param pta -- an object represents external user
	 */
	public void update(PartnerToAssistments pta);
	
	/**
	 * Get user profile information from ASSISTments
	 * @param externalRef -- Identifier for this user from ASSISTments
	 * @return User object -- It contains user information.
	 */
	public User getUserProfile(String externalRef);
	
	public User getUserProfileById(long id);
	
	public boolean isTeacherExist(String email);
}
