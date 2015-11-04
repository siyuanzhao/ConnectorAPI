package org.assistments.service.domain;

import java.io.Serializable;

public class StudentReportEntry implements Serializable {

	private static final long serialVersionUID = 531519497710427167L;
	String encodedProblemId;
	long decodedProblemId;
	String problemBody;
	String myAnswer;
	double correct;
	String correctPercent;
	String classAverage;
	long hintUsage;
	boolean isCompleted;
	String teacherComment;
	
	public String getEncodedProblemId() {
		return encodedProblemId;
	}
	public void setEncodedProblemId(String encodedProblemId) {
		this.encodedProblemId = encodedProblemId;
	}
	public long getDecodedProblemId() {
		return decodedProblemId;
	}
	public void setDecodedProblemId(long decodedProblemId) {
		this.decodedProblemId = decodedProblemId;
	}
	public String getProblemBody() {
		return problemBody;
	}
	public void setProblemBody(String problemBody) {
		this.problemBody = problemBody;
	}
	public String getMyAnswer() {
		return myAnswer;
	}
	public void setMyAnswer(String myAnswer) {
		this.myAnswer = myAnswer;
	}
	public String getClassAverage() {
		return classAverage;
	}
	public void setClassAverage(String classAverage) {
		this.classAverage = classAverage;
	}
	public long getHintUsage() {
		return hintUsage;
	}
	public void setHintUsage(long hintUsage) {
		this.hintUsage = hintUsage;
	}
	public double getCorrect() {
		return correct;
	}
	public void setCorrect(double correct) {
		this.correct = correct;
	}
	public boolean isCompleted() {
		return isCompleted;
	}
	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	public String getCorrectPercent() {
		return correctPercent;
	}
	public void setCorrectPercent(String correctPercent) {
		this.correctPercent = correctPercent;
	}
	public String getTeacherComment() {
		return teacherComment;
	}
	public void setTeacherComment(String teacherComment) {
		this.teacherComment = teacherComment;
	}
	
}
