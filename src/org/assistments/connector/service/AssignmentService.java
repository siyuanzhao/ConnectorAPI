package org.assistments.connector.service;

import java.util.List;

import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.domain.Assignment;

/**
 * This interface contains operations on ASSISTments assignment 
 * and manages the associations between ASSISTments assignment and Partner assignment.
 * @author szhao
 *
 */
public interface AssignmentService {
	/**
	 * Transfer the assignment from Partner to ASSISTments.
	 * First it checks if partnerExternalRef already exists. 
	 * If it doesn't, a new assignment will be created inside ASSISTments.
	 * Store the association between new ASSISTments assignment and Partner assignment into database.
	 * If it does exist, external reference for this assignment will be fetched from database.
	 * @param problemSetId -- problem set id from ASSISTments
	 * @param stuClassRef -- external reference for the student class from ASSISTments
	 * @param partnerExternalRef -- partner external reference for the assignment from Partner
	 * @param partnerAccessToken -- partner access token for the assignment from Partner
	 * @param note -- To keep extra information
	 * @return external reference for the assignment from ASSISTments
	 */
	public String transferClassAssignment(String problemSetId, String stuClassRef,
			String partnerExternalRef, String partnerAccessToken, String note);

	/**
	 * Create a new assignment inside ASSISTments. 
	 * Store the association between ASSISTments assignment and Partner assignment into database.
	 *  It doesn't check if Partner assignment already exists
	 * @param problemSetId -- problem set id from ASSISTments
	 * @param stuClassRef -- external reference for the student class from ASSISTments
	 * @param partnerExternalRef -- partner external reference for the assignment from Partner
	 * @param partnerAccessToken -- partner access token for the assignment from Partner
	 * @param note -- To keep extra information
	 * @return external reference for the assignment from ASSISTments
	 */
	public String createClassAssignment(String problemSetId, String classRef, 
			String partnerExternalRef, String partnerAccessToken, String note);
	
	/**
	 * Get the URL needed to run the assignment using ASSISTments' Tutor
	 * @param assignmentRef -- External reference for the assignment from ASSISTments
	 * @param onExit -- After running the required assignment, the Tutor redirects to this URL.
	 * @return encoded URL
	 */
	public String getAssignment(String assignmentRef, String onExit);
	
	/**
	 * Get all class assignments for a class
	 * @param classRef -- external reference for a class from ASSISTments
	 * @return a list of class assignments
	 */
	public List<org.assistments.service.domain.Assignment> getClassAssignments(String classRef);
	
	/**
	 * Find a certain external assignment based on column name and value 
	 * @param cn -- column name. It comes from table partner_to_assistments_links (partner_external_references)
	 * @param value -- value for the column
	 * @return a list of external assignment
	 * @throws ReferenceNotFoundException -- If nothing is found, this exception will be thrown out.
	 */
	public List<PartnerToAssistments> find(PartnerToAssistments.ColumnNames cn, String value)
		throws ReferenceNotFoundException;
	
	/**
	 * Find an assingment object based on unique id for an assignment
	 * @param assignmentRef -- unique id for an assignment
	 * @return assignment object
	 */
	public Assignment find(String assignmentRef) throws ReferenceNotFoundException;
	/**
	 * Check if Partner assignment already exists based on partner_external_reference
	 * @param partnerExternalRef -- partner external reference for this assignment from Partner.
	 * @return true -- if the external assignment already exists.
	 * @return false -- if the external assignment doesn't exist.
	 */
	public boolean isExternalAssignmentExists(String partnerExternalRef);
	
	/**
	 * Update external assignment in bridge table
	 * @param pta -- one instance from bridge table and it indicates one external assignment
	 */
	public void updateExternalAssignment(PartnerToAssistments pta);
}
