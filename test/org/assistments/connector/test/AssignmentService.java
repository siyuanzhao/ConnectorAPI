package org.assistments.connector.test;

import java.util.Iterator;
import java.util.List;

import org.assistments.connector.service.impl.AssignmentServiceImpl;
import org.assistments.service.domain.Assignment;

public class AssignmentService {

	public static void main(String[] args) {
		org.assistments.connector.service.AssignmentService as = new AssignmentServiceImpl("Direct-Ref", "");
		
		String classRef = "a759dc81916f7d109b18a0e6d93b712c";
		List<Assignment> assignments = as.getClassAssignments(classRef);
		
		Iterator<Assignment> iter = assignments.iterator();
		while(iter.hasNext()) {
			Assignment assignment = iter.next();
			
			System.out.println(assignment.getExternalReference() + " - " + assignment.getPs().getName());
		}
	}
}
