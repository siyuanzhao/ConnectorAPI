package org.assistments.service.domain;

import java.sql.Date;
import java.sql.Timestamp;

public class AssignmentLog {
	public static final String CLASS_ASSIGNMENT = "ClassAssignment";
	int id;
	int assignmentId;
	int userId;
	Timestamp startTime;
	Timestamp endTime;
	int instance;
//	String sequence;
	int effortScore;
//	String variables;
	Date lastWorkedOn;
	String masteryStatus;
	String assignmentType;
	int statusId;
	boolean everExceededLimit;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getAssignmentId() {
		return assignmentId;
	}
	public void setAssignmentId(int assignmentId) {
		this.assignmentId = assignmentId;
	}
	public int getUserId() {
		return userId;
	}
	public void setUserId(int userId) {
		this.userId = userId;
	}
	public Timestamp getStartTime() {
		return startTime;
	}
	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}
	public Timestamp getEndTime() {
		return endTime;
	}
	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}
	public int getInstance() {
		return instance;
	}
	public void setInstance(int instance) {
		this.instance = instance;
	}
	public int getEffortScore() {
		return effortScore;
	}
	public void setEffortScore(int effort_score) {
		this.effortScore = effort_score;
	}
	public Date getLastWorkedOn() {
		return lastWorkedOn;
	}
	public void setLastWorkedOn(Date lastWorkedOn) {
		this.lastWorkedOn = lastWorkedOn;
	}
	public String getMasteryStatus() {
		return masteryStatus;
	}
	public void setMasteryStatus(String masteryStatus) {
		this.masteryStatus = masteryStatus;
	}
	public String getAssignmentType() {
		return assignmentType;
	}
	public void setAssignmentType(String assignmentType) {
		this.assignmentType = assignmentType;
	}
	public int getStatusId() {
		return statusId;
	}
	public void setStatusId(int statusId) {
		this.statusId = statusId;
	}
	public boolean isEverExceededLimit() {
		return everExceededLimit;
	}
	public void setEverExceededLimit(boolean everExceededLimit) {
		this.everExceededLimit = everExceededLimit;
	}
	public static String getClassAssignment() {
		return CLASS_ASSIGNMENT;
	}
}
