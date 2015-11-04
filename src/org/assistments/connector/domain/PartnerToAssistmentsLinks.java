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
	@Override
	public int getId() {
		return id;
	}
	@Override
	public void setId(int id) {
		this.id = id;
	}
	@Override
	public String getApiPartnerReference() {
		return apiPartnerReference;
	}
	@Override
	public void setApiPartnerReference(String apiPartnerReference) {
		this.apiPartnerReference = apiPartnerReference;
	}
	@Override
	public int getExternalRefernceTypeId() {
		return externalRefernceTypeId;
	}
	@Override
	public void setExternalRefernceTypeId(int externalRefernceTypeId) {
		this.externalRefernceTypeId = externalRefernceTypeId;
	}
	@Override
	public String getAssistmentsExternalRefernce() {
		return assistmentsExternalRefernce;
	}
	@Override
	public void setAssistmentsExternalRefernce(String assistmentsExternalRefernce) {
		this.assistmentsExternalRefernce = assistmentsExternalRefernce;
	}
	@Override
	public String getAssistmentsAccessToken() {
		return assistmentsAccessToken;
	}
	@Override
	public void setAssistmentsAccessToken(String assistmentsAccessToken) {
		this.assistmentsAccessToken = assistmentsAccessToken;
	}
	@Override
	public String getPartnerExternalReference() {
		return partnerExternalReference;
	}
	@Override
	public void setPartnerExternalReference(String partnerExternalReference) {
		this.partnerExternalReference = partnerExternalReference;
	}
	@Override
	public String getPartnerAccessToken() {
		return partnerAccessToken;
	}
	@Override
	public void setPartnerAccessToken(String partnerAccessToken) {
		this.partnerAccessToken = partnerAccessToken;
	}
	@Override
	public String getNote() {
		return note;
	}
	@Override
	public void setNote(String note) {
		this.note = note;
	}
	
}
