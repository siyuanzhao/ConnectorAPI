package org.assistments.service.controller;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.domain.Assignment;

/**
 * This interface represents Assignment Management in ASSISTments Service
 * @author szhao
 *
 */
public interface AssignmentController {
	/**
	 * Create a new class assignment
	 * @param problemSetId -- problem set identifier
	 * @param classRef -- student class identifier
	 * @return assignment identifier
	 * @throws Exception
	 */
	public String createAssignment(String problemSetId, String classRef) 
			throws Exception;
	
	/**
	 * Get the URL needed to run the assignment using ASSISTments' Tutor
	 * @param assignmentRef
	 * @param onExit
	 * @return
	 * @throws Exception
	 */
	public String getAssignment(String assignmentRef, String onExit) 
			throws Exception;
	/**
	 * Find an assignment object based on unique id for an assignment
	 * @param assignmentRef -- unique id for an assignment
	 * @return assignment object
	 * @throws ReferenceNotFoundException
	 */
	public Assignment find(String assignmentRef) throws ReferenceNotFoundException;
	
	public String getAssignment(String assignmentRef, String onExit, String logoUrl, String whiteLabeled, String accountName);
}
