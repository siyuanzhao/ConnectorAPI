package org.assistments.connector.service.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.assistments.connector.controller.ExternalAssignmentDAO;
import org.assistments.connector.controller.PartnerToAssistmentsDAO;
import org.assistments.connector.domain.ExternalAssignment;
import org.assistments.connector.domain.PartnerToAssistments;
import org.assistments.connector.domain.PartnerToAssistments.ColumnNames;
import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.service.AssignmentService;
import org.assistments.dao.ConnectionFactory;
import org.assistments.service.controller.AssignmentController;
import org.assistments.service.controller.impl.AssignmentControllerWebImpl;
import org.assistments.service.dao.ClassDao;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.ExternalReferenceNotFoundException;
import org.assistments.service.dao.jdbc.JdbcClassDao;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcFolderUtil;
import org.assistments.service.domain.Assignment;
import org.assistments.service.domain.ProblemSet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

public class AssignmentServiceImpl implements AssignmentService {
	
	String apiPartnerRef;
	String accessToken;
	PartnerToAssistmentsDAO externalAssignmentDAO;
	AssignmentController assignmentController;
	String errorSourceType = "Connector";
	ExternalReferenceDao refDao;
	JdbcTemplate jdbcTemplate;
	
	private AssignmentServiceImpl() {
	}
	
	public AssignmentServiceImpl(String apiPartnerRef, String accessToken) {
		this.apiPartnerRef = apiPartnerRef;
		this.accessToken = accessToken;
		DataSource dataSource = ConnectionFactory.getDataSource();
		refDao = new JdbcExternalReferenceDao(dataSource);
		externalAssignmentDAO = new ExternalAssignmentDAO(apiPartnerRef);
		assignmentController = new AssignmentControllerWebImpl(apiPartnerRef, accessToken);
		jdbcTemplate = new JdbcTemplate(dataSource);
	}
	
	public static PartnerToAssistments getFromAssignmentRef(String apiPartnerRef, String assignmentRef) throws ReferenceNotFoundException {
		AssignmentServiceImpl as = new AssignmentServiceImpl();
		as.apiPartnerRef = apiPartnerRef;
		as.externalAssignmentDAO = new ExternalAssignmentDAO(apiPartnerRef);
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
		externalAssignmentDAO.add(assignment);
		
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
		assignments = externalAssignmentDAO.find(cn, value);

		return assignments;
	}

	@Override
	public boolean isExternalAssignmentExists(String partnerExternalRef) {
		boolean result = false;

		result  = externalAssignmentDAO.isExists(ColumnNames.PARTNER_EXTERNAL_REFERENCE, partnerExternalRef);

		return result;
	}

	@Override
	public org.assistments.domain.Assignment find(String assignmentRef) throws ReferenceNotFoundException {
		return assignmentController.find(assignmentRef);
	}

	@Override
	public void updateExternalAssignment(PartnerToAssistments pta) {
		externalAssignmentDAO.update(pta);
	}

	@Override
	public List<Assignment> getClassAssignments(String classRef) {
		long classSectionId = 0;
		try {
			classSectionId = refDao.getDbidFromReference(classRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
		DataSource dataSource = ConnectionFactory.getDataSource();
		JdbcFolderUtil folderUtil = new JdbcFolderUtil(dataSource);
		ClassDao classDao = new JdbcClassDao(dataSource, folderUtil);
		//get class id from class section id
		long classId = classDao.findBySectionId(classSectionId).getId();
		String sql = "select ca.id as assignment_id, ca.student_class_id, ca.position, ca.due_date, "
				+ "s.id as problem_set_id, s.name from class_assignments ca "
				+ "join sequences s on ca.sequence_id = s.id where ca.student_class_id = ?";
		
		List<Assignment> result = 
				jdbcTemplate.query(sql, new ResultSetExtractor<List<Assignment>>() {

			@Override
			public List<Assignment> extractData(ResultSet rs) throws SQLException, DataAccessException {
				List<Assignment> assignments = new ArrayList<>();
				while(rs.next()) {
					ProblemSet ps = new ProblemSet();
					ps.setDecodedID(rs.getInt("problem_set_id"));
					ps.setName(rs.getString("name"));
					
					Assignment assignment = new Assignment();
					assignment.setId(rs.getLong("assignment_id"));
					assignment.setPs(ps);
					assignment.setStudentClassId(rs.getLong("student_class_id"));
					assignment.setPosition(rs.getInt("position"));
					assignment.setDueDate(rs.getTimestamp("due_date"));
					String ref = refDao.getReferenceForEntity("class_assignments", apiPartnerRef, assignment.getId());
					assignment.setExternalReference(ref);
					assignments.add(assignment);
				}
				return assignments;
			}
			
		}, classId);
		
		
		return result;
	}
}
