package org.assistments.connector.service.impl;

import java.util.List;

import org.assistments.connector.controller.ExternalShareLinkDAO;
import org.assistments.connector.controller.PartnerToAssistmentsDAO;
import org.assistments.connector.domain.ExternalShareLink;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.service.ShareLinkService;
import org.assistments.connector.utility.ApplicationConfig;
import org.assistments.service.controller.ShareLinkController;
import org.assistments.service.controller.impl.ShareLinkControllerDAOImpl;
import org.assistments.service.domain.ShareLink;

public class ShareLinkServiceImpl implements ShareLinkService {
	
	static {
		ApplicationConfig.loadSpringConfig();
	}
	
	String apiPartnerRef;
	PartnerToAssistmentsDAO ptaDAO;

	ShareLinkController slc;
	
	public ShareLinkServiceImpl() {
	
	}
	
	public ShareLinkServiceImpl(String apiPartnerRef) {
		this.apiPartnerRef = apiPartnerRef;
		ptaDAO = new ExternalShareLinkDAO(apiPartnerRef);
		slc = new ShareLinkControllerDAOImpl(apiPartnerRef);
		
	}

	@Override
	public String create(String userId, String problemSetId, String recipient, boolean reShareable) {
		String ref = new String();
		ref = slc.create(userId, problemSetId, recipient, reShareable);
		return ref;
	}
	
	@Override
	public String create(String userId, String problemSetId, String recipient,
			boolean reShareable, String url, String form, boolean isAssistmentsVerified) {
		String ref = new String();
		ref = slc.create(userId, problemSetId, recipient, reShareable, url, form, isAssistmentsVerified);
		return ref;
	}

	@Override
	public void addExternalShareLink(String shareLinkRef, String userToken,
			String partnerExternalRef, String partnerToken, String note) {
		PartnerToAssistments pta = new ExternalShareLink(apiPartnerRef);
		pta.setAssistmentsExternalRefernce(shareLinkRef);
		pta.setAssistmentsAccessToken(userToken);
		pta.setPartnerExternalReference(partnerExternalRef);
		pta.setPartnerAccessToken(partnerToken);
		pta.setNote(note);

		ptaDAO.add(pta);
	
	}

	@Override
	public ShareLink find(String shareLinkRef) throws ReferenceNotFoundException {
		ShareLink sl = slc.find(shareLinkRef);
		return sl;
	}

	@Override
	public List<PartnerToAssistments> find(ColumnNames cn, String value) throws ReferenceNotFoundException {
		List<PartnerToAssistments> list = ptaDAO.find(cn, value);

		return list;
	}

	
}
