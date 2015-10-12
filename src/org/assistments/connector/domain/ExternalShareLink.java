package org.assistments.connector.domain;

import org.assistments.connector.utility.Constants;

public class ExternalShareLink extends PartnerToAssistmentsLinks implements Comparable<ExternalShareLink>{
	
	public ExternalShareLink(String partnerRef) {
		this.externalRefernceTypeId = Constants.SHARE_LINK_TYPE_ID;
		this.apiPartnerReference = partnerRef;
	}
	
	@Override
	public int compareTo(ExternalShareLink e) {
		if(this.id > e.id) {
			return 1;
		} else if (this.id == e.id){
			return 0;
		} else {
			return -1;
		}
	}
}
