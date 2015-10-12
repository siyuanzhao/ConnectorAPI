package org.assistments.connector.domain;

public abstract class PartnerToAssistmentsLinks implements PartnerToAssistments {
	
	int id;
	String apiPartnerReference; 
	int externalRefernceTypeId;
	String assistmentsExternalRefernce; // external_refernce from ASSISTMents
	String assistmentsAccessToken; // acccess_token from ASSISTments
	String partnerExternalReference; // reference of outside system.
	String partnerAccessToken; // access_token of outside system
	String note; // for furture use: uninstall the app, note here
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getApiPartnerReference() {
		return apiPartnerReference;
	}
	public void setApiPartnerReference(String apiPartnerReference) {
		this.apiPartnerReference = apiPartnerReference;
	}
	public int getExternalRefernceTypeId() {
		return externalRefernceTypeId;
	}
	public void setExternalRefernceTypeId(int externalRefernceTypeId) {
		this.externalRefernceTypeId = externalRefernceTypeId;
	}
	public String getAssistmentsExternalRefernce() {
		return assistmentsExternalRefernce;
	}
	public void setAssistmentsExternalRefernce(String assistmentsExternalRefernce) {
		this.assistmentsExternalRefernce = assistmentsExternalRefernce;
	}
	public String getAssistmentsAccessToken() {
		return assistmentsAccessToken;
	}
	public void setAssistmentsAccessToken(String assistmentsAccessToken) {
		this.assistmentsAccessToken = assistmentsAccessToken;
	}
	public String getPartnerExternalReference() {
		return partnerExternalReference;
	}
	public void setPartnerExternalReference(String partnerExternalReference) {
		this.partnerExternalReference = partnerExternalReference;
	}
	public String getPartnerAccessToken() {
		return partnerAccessToken;
	}
	public void setPartnerAccessToken(String partnerAccessToken) {
		this.partnerAccessToken = partnerAccessToken;
	}
	public String getNote() {
		return note;
	}
	public void setNote(String note) {
		this.note = note;
	}
	
}
