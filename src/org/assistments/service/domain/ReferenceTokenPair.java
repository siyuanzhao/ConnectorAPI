package org.assistments.service.domain;

public class ReferenceTokenPair {
	String externalRef;
	String accessToken;
	
	public ReferenceTokenPair(String externalRef, String accessToken) {
		this.externalRef = externalRef;
		this.accessToken = accessToken;
	}

	public String getExternalRef() {
		return externalRef;
	}

	public void setExternalRef(String externalRef) {
		this.externalRef = externalRef;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}
}
