package org.assistments.connector.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.controller.ExternalUserDAO;
import org.assistments.connector.controller.PartnerToAssistmentsDAO;
import org.assistments.connector.domain.ExternalUser;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.exception.TransferUserException;
import org.assistments.connector.service.AccountService;
import org.assistments.connector.service.SchoolService;
import org.assistments.service.controller.AccountController;
import org.assistments.service.controller.ErrorLogController;
import org.assistments.service.controller.impl.AccountControllerWebImpl;
import org.assistments.service.domain.ReferenceTokenPair;
import org.assistments.service.domain.User;

public class AccountServiceImpl implements AccountService {
	String apiPartnerRef;
	AccountController ac;
	PartnerToAssistmentsDAO userDAO;
	String errorSourceType = "Connector";
	
	public AccountServiceImpl(String apiPartnerRef) {
		this.apiPartnerRef = apiPartnerRef;
		ac = new AccountControllerWebImpl(apiPartnerRef);
		userDAO = new ExternalUserDAO(apiPartnerRef);
	}
	
	@Override
	public User getUserProfile(String externalRef) {
		User user = null;
		try {
			user = ac.getUserProfile(externalRef);
		} catch (Exception e) {
			ErrorLogController.addNewError(e, errorSourceType);
			e.printStackTrace();
		}
		return user;
	}
	
	@Override
	public ReferenceTokenPair transferUser(User user, String schoolRef,
			String partnerExternalRef, String partnerAccessToken, String note) throws TransferUserException {
		String externalRef = new String();
		String accessToken = new String();
		
		if(!isExternalUserExists(partnerExternalRef)) {
			ReferenceTokenPair pair = createUser(user, partnerExternalRef, partnerAccessToken, note);
			externalRef = pair.getExternalRef();
			accessToken = pair.getAccessToken();
			SchoolService ss = new SchoolServiceImpl(apiPartnerRef, accessToken);
			ss.assignUserToSchool(externalRef, schoolRef);
			return pair;
		} else {
			try {
				List<PartnerToAssistments> list = find(ColumnNames.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef);
				externalRef = list.get(0).getAssistmentsExternalRefernce();
				accessToken = list.get(0).getAssistmentsAccessToken();
				//TODO if partnerAccessToken is changed, we should update it
			} catch (ReferenceNotFoundException e) {
				//omit this exception since this should never happen
				e.printStackTrace();
			}
		}
		ReferenceTokenPair pair = new ReferenceTokenPair(externalRef, accessToken);
		return pair;
	}

	@Override
	public ReferenceTokenPair createUser(User user, String thirdParyId,
			String thirdPartyAccessToken, String note) throws TransferUserException {
		String userRef = new String();
		
		PartnerToAssistments externalUser = new ExternalUser(apiPartnerRef);
		
		try {
			userRef = ac.createUser(user);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		String accessToken = createAccessToken(userRef);
		
		externalUser.setAssistmentsExternalRefernce(userRef);
		externalUser.setAssistmentsAccessToken(accessToken);
		externalUser.setPartnerExternalReference(thirdParyId);
		externalUser.setPartnerAccessToken(thirdPartyAccessToken);
		externalUser.setNote(note);
		
		userDAO.add(externalUser);
		
		ReferenceTokenPair pair = new ReferenceTokenPair(userRef, accessToken);
		
		return pair;
	}

	/**
	 * Create access token inside ASSISTments for this user based on user external reference
	 * Before calling this function, make sure the user does not have access token
	 * @param userRef -- use external reference inside ASSISTments
	 * @return access  token inside ASSSISTments
	 */
	@Override
	public String createAccessToken(String externalRef) {
		String accessToken = new String();
		try {
			accessToken = ac.createAccessToken(externalRef);
		} catch (Exception e) {
			// TODO userId should be something else rather than 1
			ErrorLogController.addNewError(e, errorSourceType);
			throw new RuntimeException(e);
		}
		
		return accessToken;
	}

	@Override
	public boolean isEmailInUse(String email) {
		boolean b = false;
		
		try {
			b = ac.isEmailInUse(email);
		} catch (Exception e) {
			// TODO userId should be something else rather than 1
			ErrorLogController.addNewError(e, errorSourceType);
			throw new RuntimeException(e);
		}
		
		return b;
	}
	
	@Override
	public boolean isExternalUserExists(String thirdPartyId) {
		boolean b = true;

		b = userDAO.isExists(ColumnNames.PARTNER_EXTERNAL_REFERENCE, thirdPartyId);
		
		return b;	
	}
	
	@Override
	public List<PartnerToAssistments> find(PartnerToAssistments.ColumnNames cn, String value)
			throws ReferenceNotFoundException{
		List<PartnerToAssistments> users = new ArrayList<PartnerToAssistments>();

		users = userDAO.find(cn, value);
	
		return users;
	}

	@Override
	public void update(PartnerToAssistments pta) {

		userDAO.update(pta);

	}

	@Override
	public User getUserProfileById(long id) {
		return ac.getUserProfileById(id);
	}

	@Override
	public boolean isTeacherExist(String email) {
		// TODO Auto-generated method stub
		return ac.isTeacherExist(email);
	}
}
