package org.assistments.service.domain;

public class ProblemSet {

	private String encodedID;
	private int decodedID;
	private long headSectionId;
	private String name;
	private String parameters;
	public String getEncodedID() {
		return encodedID;
	}
	public void setEncodedID(String encodedID) {
		this.encodedID = encodedID;
	}
	public int getDecodedID() {
		return decodedID;
	}
	public void setDecodedID(int decodedID) {
		this.decodedID = decodedID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public long getHeadSectionId() {
		return headSectionId;
	}
	public void setHeadSectionId(long headSectionId) {
		this.headSectionId = headSectionId;
	}
	public String getParameters() {
		return parameters;
	}
	public void setParameters(String parameters) {
		this.parameters = parameters;
	}
	
}
