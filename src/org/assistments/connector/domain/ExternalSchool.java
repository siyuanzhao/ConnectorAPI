package org.assistments.connector.domain;

import org.assistments.connector.utility.Constants;

public class ExternalSchool extends PartnerToAssistmentsLinks {

	
	public ExternalSchool(String partnerRef) {
		this.externalRefernceTypeId = Constants.EXTERNAL_SCHOOL_TYPE_ID;
		this.apiPartnerReference = partnerRef;
	}
}
