package org.assistments.connector.domain;

import org.assistments.connector.utility.Constants;

public class ExternalStudentClassSection extends PartnerToAssistmentsLinks {
	
	public ExternalStudentClassSection(String partnerRef) {
		this.externalRefernceTypeId = Constants.EXTERNAL_CLASS_SECTION_TYPE_ID;
		this.apiPartnerReference = partnerRef;
	}
	
	

}
