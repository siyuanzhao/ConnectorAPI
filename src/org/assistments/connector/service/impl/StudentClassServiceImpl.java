package org.assistments.connector.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.controller.ExternalStudentClassSectionDAO;
import org.assistments.connector.controller.PartnerToAssistmentsDAO;
import org.assistments.connector.domain.ExternalStudentClassSection;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.service.StudentClassService;
import org.assistments.service.controller.ErrorLogController;
import org.assistments.service.controller.StudentClassController;
import org.assistments.service.controller.impl.StudentClassControllerWebImpl;
import org.assistments.service.domain.StudentClass;

public class StudentClassServiceImpl implements StudentClassService {
	
	String apiPartnerRef;
	String accessToken;
	PartnerToAssistmentsDAO stuClassDAO;
	StudentClassController scc;
	String errorSourceType = "Connector";
	
	/**
	 * 
	 * @param apiPartnerRef -- API partner reference from ASSISTments
	 * @param accessToken -- 
	 */
	public StudentClassServiceImpl(String apiPartnerRef, String accessToken) {
		this.apiPartnerRef = apiPartnerRef;
		this.accessToken = accessToken;
		stuClassDAO = new ExternalStudentClassSectionDAO(apiPartnerRef);
		scc = new StudentClassControllerWebImpl(apiPartnerRef, accessToken);
	}

	@Override
	public String transferStudentClass(String studentClassName,
			String partnerExternalRef, String partnerAccessToken, String note) {
		String stuClassRef = new String();
		if(!isExternalStudentClassExists(partnerExternalRef)) {
			stuClassRef = createStudentClass(studentClassName, partnerExternalRef, partnerAccessToken, note);			
		} else {
			try {
				List<PartnerToAssistments> list = find(ColumnNames.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef);
				stuClassRef = list.get(0).getAssistmentsExternalRefernce();
			} catch (ReferenceNotFoundException e) {
				//omit this exception since this should never happen
			}
		}
		
		return stuClassRef;
	}
	
	@Override
	public String createStudentClass(String studentClassName, 
			String partnerExternalRef, String partnerAccessToken, String note) {
		StudentClass sc = new StudentClass(studentClassName);
		String stuClassRef = new String();
		try {
			stuClassRef = scc.createStudentClass(sc);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		PartnerToAssistments externalStuClass = new ExternalStudentClassSection(apiPartnerRef);
		externalStuClass.setAssistmentsExternalRefernce(stuClassRef);
		externalStuClass.setAssistmentsAccessToken(accessToken);
		externalStuClass.setPartnerExternalReference(partnerExternalRef);
		externalStuClass.setPartnerAccessToken(partnerAccessToken);
		externalStuClass.setNote(note);
		stuClassDAO.add(externalStuClass);
		return stuClassRef;
	}

	@Override
	public boolean enrollStudent(String stuRef, String stuClassRef) {
		boolean result = false;
		try {
			result = scc.enrollStudent(stuClassRef, stuRef);
		} catch (Exception e) {
			ErrorLogController.addNewError(e, errorSourceType);
			throw new RuntimeException(e);
		}
		
		return result;
	}

	@Override
	public List<String> getMembeship(String stuClassSectionRef) {
		List<String> list = scc.getClassMembership(stuClassSectionRef);
		return list;
	}

	@Override
	public List<PartnerToAssistments> find(PartnerToAssistments.ColumnNames cn, String value) 
			throws ReferenceNotFoundException{
		List<PartnerToAssistments> result = new ArrayList<PartnerToAssistments>();
		result = stuClassDAO.find(cn, value);
		
		return result;
	}

	@Override
	public boolean isExternalStudentClassExists(String partnerExternalRef) {
		boolean result = false;

		result = stuClassDAO.isExists(ColumnNames.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef);
		return result;
	}

	@Override
	public String createNewClassSection(String classRef, String sectionName, 
			String partnerExternalRef, String partnerAccessToken, String note) {
		String sectionRef = scc.createClassSection(classRef, sectionName);
		//store the mapping
		PartnerToAssistments pta = new ExternalStudentClassSection(apiPartnerRef);
		pta.setAssistmentsExternalRefernce(sectionRef);
		pta.setAssistmentsAccessToken(accessToken);
		pta.setPartnerExternalReference(partnerExternalRef);
		pta.setPartnerAccessToken(partnerAccessToken);
		pta.setNote(note);
		
		stuClassDAO.add(pta);
		return sectionRef;
	}

	@Override
	public void changeStudentClassSection(String userRef, String oldSectionRef, String newSectionRef) {
		scc.changeStudentSection(userRef, oldSectionRef, newSectionRef);
	}

	@Override
	public String getSectionName(String sectionRef) {
		return scc.getSectionName(sectionRef);
	}

	@Override
	public String getClassBySection(String sectionRef) {
		return scc.getClassForSection(sectionRef);
	}
}
