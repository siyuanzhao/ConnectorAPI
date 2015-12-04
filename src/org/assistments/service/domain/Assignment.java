package org.assistments.service.domain;

import java.sql.Timestamp;

public class Assignment {
	long id;
	ProblemSet ps;
	long studentClassId;
	int position;
	
	Timestamp dueDate;
	String externalReference;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public ProblemSet getPs() {
		return ps;
	}

	public void setPs(ProblemSet ps) {
		this.ps = ps;
	}

	public long getStudentClassId() {
		return studentClassId;
	}

	public void setStudentClassId(long studentClassId) {
		this.studentClassId = studentClassId;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Timestamp getDueDate() {
		return dueDate;
	}

	public void setDueDate(Timestamp dueDate) {
		this.dueDate = dueDate;
	}

	public String getExternalReference() {
		return externalReference;
	}

	public void setExternalReference(String externalReference) {
		this.externalReference = externalReference;
	}	
	
}
