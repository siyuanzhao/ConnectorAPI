package org.assistments.service.domain;

public class Answer {
	long id;
	String value;
	boolean isCorrect;
	String incorrectMessage;
	long problemId;
	long positon;
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public boolean isCorrect() {
		return isCorrect;
	}
	public void setCorrect(boolean isCorrect) {
		this.isCorrect = isCorrect;
	}
	public String getIncorrectMessage() {
		return incorrectMessage;
	}
	public void setIncorrectMessage(String incorrectMessage) {
		this.incorrectMessage = incorrectMessage;
	}
	public long getProblemId() {
		return problemId;
	}
	public void setProblemId(long problemId) {
		this.problemId = problemId;
	}
	public long getPositon() {
		return positon;
	}
	public void setPositon(long positon) {
		this.positon = positon;
	}
}
