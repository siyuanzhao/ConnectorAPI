package org.assistments.service.controller;

import java.util.List;

import org.assistments.service.domain.User;

public interface AssignmentLogController {

	public List<User> getNotStartedStudents(String assignmentRef);
	
	public List<User> getInProgressStudents(String assignmentRef);
	
	public List<User> getCompleteStudents(String assignmentRef);
	
	public void deleteAssignmentProgress(long userId, long assignmentId);
	
	public void completeAssignmentProgress(long userId, long assignmentId);
}
