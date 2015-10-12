package org.assistments.service.controller;

import java.util.List;

import org.assistments.service.domain.StudentClass;

/**
 * This interface represents Student Class Management in ASSISTments Service
 * @author szhao
 *
 */
public interface StudentClassController {

	/**
	 * Create a student class and a default class section
	 * @param studentClass -- it contains information about the student class
	 * @return identifier for the student class section
	 * @throws Exception
	 */
	public String createStudentClass(StudentClass studentClass) 
			throws Exception;
	
	/**
	 * Enroll student in a class
	 * @param classRef -- identifier for the student class section
	 * @param studentRef -- identifier for the student
	 * @return true -- if successfully
	 * @return false -- if unsuccessfully
	 * @throws Exception
	 */
	public boolean enrollStudent(String classSectionRef, String studentRef)
			throws Exception;
	
	/**
	 * Get all students in a class section
	 * @param classSectionRef -- class reference for a student class section from ASSISTments
	 * @return a list of user references
	 */
	List<String> getClassMembership(String classSectionRef);
	
	/**
	 * Create a new class section in a student class
	 * @param studentClassRef -- external reference for the student class from ASSISTments
	 * @param sectionName -- section name
	 * @return external reference for the class section from ASSISTments
	 */
	public String createClassSection(String studentClassRef, String sectionName);
	
	/**
	 * Update name for a student class section
	 * @param sectionRef -- external reference for the student class section
	 * @param sectionName -- section name
	 */
	public void updateSectionName(String sectionRef, String sectionName);
	
	/**
	 * Change a student from one section to another section in the same student class
	 * @param userRef -- external reference for the user from ASSISTments
	 * @param oldSectionRef -- current section reference the student is in
	 * @param newSectionRef -- new section reference
	 */
	public void changeStudentSection(String userRef, String oldSectionRef, String newSectionRef);
	
	/**
	 * Get all student class section references in a student class
	 * @param studentClassRef -- external reference for a student class
	 * @return a list of external references for all student sections
	 */
	public List<String> getClassSections(String studentClassRef);
	
	/**
	 * Get the name of a section
	 * @param sectionRef -- external reference for a class section from ASSISTments
	 * @return the section name
	 */
	public String getSectionName(String sectionRef);
	
	public String getClassForSection(String sectionRef);
}
