package org.assistments.connector.service;

import java.util.List;

import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.exception.ReferenceNotFoundException;

/**
 * This interface contains operations on ASSISTments student class 
 * and manages the associations between ASSISTments student class and Partner student class.
 * @author szhao
 *
 */
public interface StudentClassService {
	
	/**
	 * Transfer the student class from Partner to ASSISTments.
	 * First it checks if partnerExternalRef already exists. 
	 * If it doesn't, a new student class will be created inside ASSISTments.
	 * Store the association between new ASSISTments student class and Partner student class into database.
	 * If it does exist, external reference for the student class will be fetched from database.
	 * @param studentClassName -- Student class name
	 * @param partnerExternalRef -- Partner external reference for the student class from Partner
	 * @param partnerAccessToken -- Partner access token for the student class from Partner
	 * @param note -- To keep extra information
	 * @return external reference for this student class from ASSISTments
	 */
	public String transferStudentClass(String studentClassName, 
			String partnerExternalRef, String partnerAccessToken, String note);

	/**
	 * Create a new student class inside ASSISTments. 
	 * Store the association between ASSISTments student class and Partner student class into database.
	 *  It doesn't check if Partner student class already exists
	 * @param studentClassName -- Student class name
	 * @param partnerExternalRef -- Partner external reference for the student class from Partner
	 * @param partnerAccessToken -- Partner access token for the student class from Partner
	 * @param note -- To keep extra information
	 * @return external reference for this student class from ASSISTments
	 */
	public String createStudentClass(String studentClassName, 
			String partnerExternalRef, String partnerAccessToken, String note);
	
	/**
	 * Enroll a student in a class
	 * @param studentRef -- external reference for the student from ASSISTments
	 * @param studentClassRef -- external reference for the student class from ASSISTments
	 * @return true -- if student is enrolled successfully
	 * @return false -- if something goes wrong
	 */
	public boolean enrollStudent(String studentRef, String studentClassRef);
	
	/**
	 * Get the members of a class
	 * @param studentClassRef -- External reference for the student class from ASSISTments
	 * @return a list of external reference for students from ASSISTments
	 */
	public List<String> getMembeship(String studentClassRef);
	
	/**
	 * Find a certain external student class based on column name and value 
	 * @param cn -- column name. It comes from table partner_to_assistments_links (partner_external_references)
	 * @param value -- value for the column
	 * @return a list of external student
	 * @throws ReferenceNotFoundException -- If nothing is found, this exception will be thrown out.
	 */
	public List<PartnerToAssistments> find(PartnerToAssistments.ColumnNames cn, String value)
		throws ReferenceNotFoundException;
	
	/**
	 * Check if Partner student class already exists based on partner_external_reference
	 * @param partnerExternalRef -- partner external reference for this student class from Partner.
	 * @return true -- if the external student class already exists.
	 * @return false -- if the external student class doesn't exist.
	 */
	public boolean isExternalStudentClassExists(String partnerExternalRef);
	
	/**
	 * Create a new class section in a student class
	 * Store the mapping information into the table
	 * @param classRef -- external reference for a student class from ASSISTments
	 * @param sectionName -- class section name
	 * @return external reference for the class section
	 */
	public String createNewClassSection(String classRef, String sectionName,
			String partnerExternalRef, String accessToken, String note);
	
	/**
	 * Change a student from a class section to another class section
	 * @param userRef -- external reference for a student from ASSISTments
	 * @param oldSectionRef -- old section reference
	 * @param newSectionRef -- new section reference
	 */
	public void changeStudentClassSection(String userRef, String oldSectionRef, String newSectionRef);
	
	/**
	 * Get the name of a section
	 * @param sectionRef -- external reference for a class section from ASSISTments
	 * @return the section name
	 */
	public String getSectionName(String sectionRef);
	
	/**
	 * Get class reference from a section reference
	 * @param sectionRef -- external reference for a section from ASSISTments
	 * @return external reference for a class from ASSIStments
	 */
	public String getClassBySection(String sectionRef);
}
