package org.assistments.connector.domain;

import org.assistments.connector.utility.Constants;

public class ExternalAssignment extends PartnerToAssistmentsLinks {
	
	public ExternalAssignment(String partnerRef) {
		this.externalRefernceTypeId = Constants.EXTERNAL_ASSIGNMENT_TYPE_ID;
		this.apiPartnerReference = partnerRef;
	}
	
	
}
