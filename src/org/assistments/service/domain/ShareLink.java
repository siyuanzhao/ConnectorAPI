package org.assistments.service.domain;

public class ShareLink {
	
	// It means anyone can create an assignment from the share link
	public static final String GENERIC = "generic";
	public static final String VERIFIED = "verified";
	public static final String PERSONALIZED = "personalized";

	private ProblemSet problemSet;
	private User distributor;
	private String recipient;
	private boolean reShareable;
	private String url;
	private String form;
	private boolean assistmentsVerified;
	
	public ProblemSet getProblemSet() {
		return problemSet;
	}
	public void setProblemSet(ProblemSet problemSet) {
		this.problemSet = problemSet;
	}
	public User getDistributor() {
		return distributor;
	}
	public void setDistributor(User distributor) {
		this.distributor = distributor;
	}
	public String getRecipient() {
		return recipient;
	}
	public void setRecipient(String recipient) {
		this.recipient = recipient;
	}
	public boolean isReShareable() {
		return reShareable;
	}
	public void setReShareable(boolean reShareable) {
		this.reShareable = reShareable;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public boolean isAssistmentsVerified() {
		return assistmentsVerified;
	}
	public void setAssistmentsVerified(boolean assistmentsVerified) {
		this.assistmentsVerified = assistmentsVerified;
	}
	
}
