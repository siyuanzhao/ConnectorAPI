package org.assistments.connector.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.assistments.connector.controller.ExternalAssignmentDAO;
import org.assistments.connector.controller.PartnerToAssistmentsDAO;
import org.assistments.connector.domain.ExternalAssignment;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.service.AssignmentService;
import org.assistments.domain.Assignment;
import org.assistments.service.controller.AssignmentController;
import org.assistments.service.controller.impl.AssignmentControllerWebImpl;

public class AssignmentServiceImpl implements AssignmentService {
	
	String apiPartnerRef;
	String accessToken;
	PartnerToAssistmentsDAO assignmentDAO;
	AssignmentController assignmentController;
	String errorSourceType = "Connector";
	
	private AssignmentServiceImpl() {
		
	}
	
	public AssignmentServiceImpl(String apiPartnerRef, String accessToken) {
		this.apiPartnerRef = apiPartnerRef;
		this.accessToken = accessToken;
		assignmentDAO = new ExternalAssignmentDAO(apiPartnerRef);
		assignmentController = new AssignmentControllerWebImpl(apiPartnerRef, accessToken);
	}
	
	public static PartnerToAssistments getFromAssignmentRef(String apiPartnerRef, String assignmentRef) throws ReferenceNotFoundException {
		AssignmentServiceImpl as = new AssignmentServiceImpl();
		as.apiPartnerRef = apiPartnerRef;
		as.assignmentDAO = new ExternalAssignmentDAO(apiPartnerRef);
		List<PartnerToAssistments> list = as.find(ColumnNames.ASSISTMENTS_EXTERNAL_REFERENCE, assignmentRef);
		PartnerToAssistments pta = list.get(0);
		
		return pta;
	}
	
	@Override
	public String transferClassAssignment(String problemSetId,
			String stuClassRef, String partnerExternalRef,
			String partnerAccessToken, String note) {
		String assignmentRef = null;
		if(isExternalAssignmentExists(partnerExternalRef)) {
			PartnerToAssistments pta = null;
			try {
				pta = find(ColumnNames.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef).get(0);
			} catch (ReferenceNotFoundException e) {
				//This exception should never happen since I already make sure the assignment exists in system.
			}
			assignmentRef = pta.getAssistmentsExternalRefernce();
		} else {
			assignmentRef = createClassAssignment(problemSetId, stuClassRef, partnerExternalRef, partnerAccessToken, note);
		}
		return assignmentRef;
	}

	@Override
	public String createClassAssignment(String problemSetId, 
			String classRef, String partnerExternalRef, String partnerAccessToken, String note) {
		String assignmentRef = new String();
		try {
			assignmentRef = assignmentController.createAssignment(problemSetId, classRef);
		} catch (Exception e1) {
			throw new RuntimeException(e1);
		}
		PartnerToAssistments assignment = new ExternalAssignment(apiPartnerRef);
		assignment.setAssistmentsExternalRefernce(assignmentRef);
		assignment.setAssistmentsAccessToken(accessToken);
		assignment.setPartnerExternalReference(partnerExternalRef);
		assignment.setPartnerAccessToken(partnerAccessToken);
		assignment.setNote(note);
		assignmentDAO.add(assignment);
		
		return assignmentRef;
	}

	@Override
	public String getAssignment(String assignmentRef, String onExit) {
		String tutorURL = new String();
		try {
			tutorURL = assignmentController.getAssignment(assignmentRef, onExit);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		
		return tutorURL;
	}

	@Override
	public List<PartnerToAssistments> find(PartnerToAssistments.ColumnNames cn, String value) 
		throws ReferenceNotFoundException {
		List<PartnerToAssistments> assignments = new ArrayList<PartnerToAssistments>();
		assignments = assignmentDAO.find(cn, value);

		return assignments;
	}

	@Override
	public boolean isExternalAssignmentExists(String partnerExternalRef) {
		boolean result = false;

		result  = assignmentDAO.isExists(ColumnNames.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef);

		return result;
	}

	@Override
	public String getAssignment(String assignmentRef, String onExit, String logoUrl, String whiteLabeled,
			String accountName) {
		return assignmentController.getAssignment(assignmentRef, onExit, logoUrl, whiteLabeled, accountName);
	}

	@Override
	public Assignment find(String assignmentRef) throws ReferenceNotFoundException {
		return assignmentController.find(assignmentRef);
	}

	@Override
	public void updateExternalAssignment(PartnerToAssistments pta) {
		assignmentDAO.update(pta);
	}
}
