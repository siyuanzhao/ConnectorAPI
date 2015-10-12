package org.assistments.service.controller.impl;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.sql.DataSource;

import org.assistments.dao.ConnectionFactory;
import org.assistments.domain.Assignment;
import org.assistments.service.controller.AssignmentLogController;
import org.assistments.service.dao.AssignmentDao;
import org.assistments.service.dao.AssignmentNotFoundException;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.ExternalReferenceNotFoundException;
import org.assistments.service.dao.jdbc.JdbcAssignmentDao;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcFolderUtil;
import org.assistments.service.domain.AssignmentLog;
import org.assistments.service.domain.User;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

/**
 * This class represents Assignment Log Management
 * This class is never used by Direct, LTI and Edmodo, and I should make it as interface.
 * @author szhao
 *
 */
public class AssignmentLogControllerDaoImpl implements AssignmentLogController {
	
	JdbcTemplate jdbcTemplate;
	ExternalReferenceDao refDao;
	AssignmentDao assignmentDao;
	
	public AssignmentLogControllerDaoImpl() {
		DataSource ds = ConnectionFactory.getDataSource();
		jdbcTemplate = new JdbcTemplate(ds);
		refDao = new JdbcExternalReferenceDao(ds);
		JdbcFolderUtil folderUtil = new JdbcFolderUtil(ds);
		assignmentDao = new JdbcAssignmentDao(ds, folderUtil);
	}
	
	public List<User> getNotStartedStudents(String assignmentRef) {
		long assignmentId;
		try {
			assignmentId = refDao.getDbidFromReference(assignmentRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		Assignment assignment = null;
		try {
			assignment = assignmentDao.findClassAssignmentById(assignmentId);
		} catch (AssignmentNotFoundException e) {
			throw new RuntimeException(e);
		}
		
		long studentClassId = assignment.getClassId();
		
		final List<User> list = new ArrayList<>();
		String sql = "select enrollments.student_id, enrollments.student_class_section_id, user_details.user_id, "
		+ "user_details.first_name, user_details.middle_name, user_details.last_name, users.login "
		+ "from enrollments inner join user_roles on enrollments.student_id = user_roles.id "
		+ "inner join user_details on user_roles.user_id = user_details.user_id inner join users on user_details.user_id = users.id "
		+ "where student_class_id = ? and users.id NOT IN "
		+ "(select user_id from assignment_logs where assignment_logs.assignment_id=?) "
		+ "and enrollments.enrollment_state_id = 1 "
		+ "order by enrollment_state_id, upper(user_details.last_name), upper(user_details.first_name), "
		+ "upper(user_details.middle_name), upper(users.login)";
		
		jdbcTemplate.query(sql, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getLong("user_id"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					list.add(user);
				}
				return null;
			}
		}, studentClassId, assignmentId);
		
		return list;
	}
	
	public List<User> getInProgressStudents(String assignmentRef) {
		long assignmentId;
		try {
			assignmentId = refDao.getDbidFromReference(assignmentRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
		final List<User> list = new ArrayList<>();
		
		String sql = "select assignment_logs.assignment_id, assignment_logs.user_id,  assignment_logs.start_time, assignment_logs.end_time, "
				+ "user_details.first_name, user_details.middle_name, user_details.last_name, users.login "
				+ "from assignment_logs "
				+ "inner join user_details on assignment_logs.user_id = user_details.user_id "
				+ "inner join users on user_details.user_id = users.id "
				+ "where assignment_logs.assignment_id=? and "
				+ "assignment_logs.start_time is not null and assignment_logs.end_time is null";
		
		jdbcTemplate.query(sql, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getLong("user_id"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					list.add(user);
				}
				return null;
			}
		}, assignmentId);
		
		return list;
	}
	
	public List<User> getCompleteStudents(String assignmentRef) {
		long assignmentId;
		try {
			assignmentId = refDao.getDbidFromReference(assignmentRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
		final List<User> list = new ArrayList<>();
		
		String sql = "select assignment_logs.assignment_id, assignment_logs.user_id,  assignment_logs.start_time, assignment_logs.end_time, "
				+ "user_details.first_name, user_details.middle_name, user_details.last_name, users.login "
				+ "from assignment_logs "
				+ "inner join user_details on assignment_logs.user_id = user_details.user_id "
				+ "inner join users on user_details.user_id = users.id "
				+ "where assignment_logs.assignment_id=? and assignment_logs.start_time "
				+ "is not null and assignment_logs.end_time is not null";
		
		jdbcTemplate.query(sql, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					User user = new User();
					user.setId(rs.getLong("user_id"));
					user.setFirstName(rs.getString("first_name"));
					user.setLastName(rs.getString("last_name"));
					list.add(user);
				}
				return null;
			}
		}, assignmentId);
		
		return list;
	}

	public static AssignmentLog find(int assignmentId, int userId) {
		AssignmentLog al = new AssignmentLog();
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM assignment_logs where assignment_id = ? and user_id = ?";
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, assignmentId);
			pstm.setInt(2, userId);
			rs = pstm.executeQuery();
			if(rs.next()) {
				al.setId(rs.getInt("id"));
				al.setAssignmentId(rs.getInt("assignment_id"));
				al.setUserId(rs.getInt("user_id"));
				al.setStartTime(rs.getTimestamp("start_time"));
				al.setEndTime(rs.getTimestamp("end_time"));
				al.setLastWorkedOn(rs.getDate("last_worked_on"));
				al.setMasteryStatus(rs.getString("mastery_status"));
				al.setAssignmentType(rs.getString("assignment_type"));
				al.setEverExceededLimit(rs.getBoolean("ever_exceeded_limit"));
			} else {
				return null;
			}
			rs.close();
			pstm.close();
			conn.close();
			return al;
		} catch(SQLException e) {
			e.printStackTrace();
			return null;
		}
		
	}
	
	public static boolean create_or_update_assignment_log(AssignmentLog assignmentLog) {
		//first check if the assignment log already exists
		Connection conn = null;
		PreparedStatement pstm = null;
		ResultSet rs = null;
		
		String query = "SELECT * FROM assignment_logs where assignment_id = ? and user_id = ?";
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setInt(1, assignmentLog.getAssignmentId());
			pstm.setInt(2, assignmentLog.getUserId());
			rs = pstm.executeQuery();
			if(rs.next()) {
				query = "UPDATE assignment_logs SET assignment_id=?, user_id=?, start_time=?, end_time=?, effort_score=?, "
						+ "last_worked_on=?, assignment_type=?, ever_exceeded_limit=? "
						+ "where assignment_id=? and user_id=?";
				PreparedStatement pstm1 = conn.prepareStatement(query);
				pstm1.setInt(1, assignmentLog.getAssignmentId());
				pstm1.setInt(2, assignmentLog.getUserId());
				pstm1.setTimestamp(3, assignmentLog.getStartTime());
				pstm1.setTimestamp(4, assignmentLog.getEndTime());
				pstm1.setInt(5, assignmentLog.getEffortScore());
				pstm1.setDate(6, assignmentLog.getLastWorkedOn());
				pstm1.setString(7, assignmentLog.getAssignmentType());
				pstm1.setBoolean(8, assignmentLog.isEverExceededLimit());
				pstm1.setInt(9, assignmentLog.getAssignmentId());
				pstm1.setInt(10, assignmentLog.getUserId());
				return pstm1.execute();
			} else {
				query = "INSERT INTO assignment_logs (assignment_id, user_id, start_time, "
						+ "end_time, effort_score, last_worked_on, assignment_type, ever_exceeded_limit) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
				PreparedStatement pstm1 = conn.prepareStatement(query);
				pstm1.setInt(1, assignmentLog.getAssignmentId());
				pstm1.setInt(2, assignmentLog.getUserId());
				pstm1.setTimestamp(3, assignmentLog.getStartTime());
				pstm1.setTimestamp(4, assignmentLog.getEndTime());
				pstm1.setInt(5, assignmentLog.getEffortScore());
				pstm1.setDate(6, assignmentLog.getLastWorkedOn());
				pstm1.setString(7, assignmentLog.getAssignmentType());
				pstm1.setBoolean(8, assignmentLog.isEverExceededLimit());
				return pstm1.execute();
			}
		} catch(SQLException e) {
			e.printStackTrace();
			return false;
		}
	}

	@Override
	public void deleteAssignmentProgress(long userId, long assignmentId) {		
		String sql = "DELETE FROM progresses WHERE user_id = ? AND class_assignment_id = ?";
		jdbcTemplate.update(sql, userId, assignmentId);
		sql = "DELETE FROM assignment_logs WHERE user_id = ? AND assignment_id = ?";
		jdbcTemplate.update(sql, userId, assignmentId);
		sql = "DELETE FROM problem_logs WHERE user_id = ? AND assignment_id = ?";
		jdbcTemplate.update(sql, userId, assignmentId);
	}

	@Override
	public void completeAssignmentProgress(long userId, long assignmentId) {
		String sql = "INSERT INTO assignment_logs(assignment_id, user_id, start_time, end_time, last_worked_on, "
				+ "mastery_status, assignment_type, ever_exceeded_limit, status_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
		Timestamp startTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		Timestamp endTime = new Timestamp(Calendar.getInstance().getTimeInMillis());
		Date lastWorkedOn = new Date(Calendar.getInstance().getTimeInMillis());
		
		jdbcTemplate.update(sql, assignmentId, userId, startTime, endTime, 
				lastWorkedOn, "", AssignmentLog.CLASS_ASSIGNMENT, false, 10);
		
	}
}
