package org.assistments.connector.service.impl;

import java.util.List;

import org.assistments.connector.service.AssignmentLogService;
import org.assistments.service.controller.AssignmentLogController;
import org.assistments.service.controller.impl.AssignmentLogControllerDaoImpl;
import org.assistments.service.domain.User;

public class AssignmentLogServiceImpl implements AssignmentLogService {

	AssignmentLogController alc;
	
	public AssignmentLogServiceImpl() {
		alc = new AssignmentLogControllerDaoImpl();
	}
	@Override
	public List<User> getNotStartedStudents(String assignmentRef) {
		return alc.getNotStartedStudents(assignmentRef);
	}

	@Override
	public List<User> getInProgressStudents(String assignmentRef) {
		return alc.getInProgressStudents(assignmentRef);
	}

	@Override
	public List<User> getCompleteStudents(String assignmentRef) {
		return alc.getCompleteStudents(assignmentRef);
	}
	@Override
	public void deleteAssignmentProgress(long userId, long assignmentId) {
		alc.deleteAssignmentProgress(userId, assignmentId);
	}
	@Override
	public void completeAssignmentProgress(long userId, long assignmentId) {
		alc.completeAssignmentProgress(userId, assignmentId);
	}

}
