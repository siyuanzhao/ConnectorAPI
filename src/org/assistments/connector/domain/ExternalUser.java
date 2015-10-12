package org.assistments.connector.domain;

import org.assistments.connector.utility.Constants;

public class ExternalUser extends PartnerToAssistmentsLinks {
	
	public ExternalUser(String partnerRef) {
		this.externalRefernceTypeId = Constants.EXTERNAL_USER_TYPE_ID;
		this.apiPartnerReference = partnerRef;
	}
	
	public ExternalUser(String partnerRef, String partnerExternalRef, String partnerAccessToken) {
		this.apiPartnerReference = partnerRef;
		this.externalRefernceTypeId = Constants.EXTERNAL_USER_TYPE_ID;
		this.partnerAccessToken = partnerAccessToken;
		this.partnerExternalReference = partnerExternalRef;
		
	}
	
}
