package org.assistments.service.domain;

public class StudentClass {
	private String courseName;
	
	public StudentClass(String courseName) {
		this.courseName = courseName;
	}
	
	public String getCourseName() {
		return courseName;
	}
	public void setCourseName(String courseName) {
		this.courseName = courseName;
	}
}
