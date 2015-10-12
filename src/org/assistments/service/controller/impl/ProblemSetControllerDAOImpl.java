package org.assistments.service.controller.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.dao.ConnectionFactory;
import org.assistments.service.controller.ProblemSetController;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.ExternalReferenceNotFoundException;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.domain.Answer;
import org.assistments.service.domain.FolderItem;
import org.assistments.service.domain.Problem;
import org.assistments.service.domain.ProblemSection;
import org.assistments.service.domain.ProblemSection.Type;
import org.assistments.service.domain.ProblemSet;
import org.assistments.service.domain.User;
import org.assistments.service.domain.FolderItem.ItemType;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ProblemSetControllerDAOImpl implements ProblemSetController {
	
	JdbcTemplate jdbcTemplate;
	DataSource ds;
	
	public ProblemSetControllerDAOImpl() {
		ds = ConnectionFactory.getDataSource();
		jdbcTemplate = new JdbcTemplate(ds);
	}
	
	public boolean isSkillBuilder(long problemSetId) {
		String sql = "SELECT sections.type FROM sections "
				+ "JOIN sequences on sections.id = sequences.head_section_id WHERE"
				+ " sequences.id = ?";
		
		String type = jdbcTemplate.queryForObject(sql, String.class, problemSetId);
		Type problemSetType = Type.fromString(type);
		switch(problemSetType) {
		case LinearMasterySection:
		case MasterySection:
			return true;
		default:
			return false;
		}
	}
	
	public boolean isPseudoSkillBuilder(long problemSetId) {
		ProblemSet ps = find(problemSetId);
		String parameters = ps.getParameters();
		
		if(parameters.contains("pseudo_skill_builder: \"true\"")) {
			return true;
		} else {
			return false;
		}
	}
	
	public long getPseudoSkillBuilderId(long problemSetId) {
		long id = 0;
		ProblemSet ps = find(problemSetId);
		String parameters = ps.getParameters();
		String pattern = "arrs_skill_builder: \"(\\d+)\"";
		Pattern r = Pattern.compile(pattern);
		Matcher m = r.matcher(parameters);
		if(m.find()) {
			id = Long.valueOf(m.group(1));
		} else {
			throw new RuntimeException("Cannot find pseudo skill builder for problem set " + problemSetId);
		}
		return id;
	}
	
	
	public List<Problem> findAllProblems(long problemSetId) {
		String sql = "select * from problems where assistment_id IN "
				+ "(select assistment_id from assistment_to_sequence_associations where sequence_id = ?)"
				+ " and scaffold_id is null";
		
		final List<Problem> list = new ArrayList<>();
		jdbcTemplate.query(sql, new ResultSetExtractor<User>() {

			@Override
			public User extractData(ResultSet rs) throws SQLException, DataAccessException {
				while (rs.next()) {
					Long problemId = rs.getLong("id");
					Problem problem = new Problem();
					problem.setId(problemId);
					problem.setName(rs.getString("name"));
					problem.setBody(rs.getString("body"));
					problem.setAssistmentId(rs.getLong("assistment_id"));
					problem.setPosition(rs.getLong("position"));
					problem.setType(org.assistments.service.domain.Problem.Type.fromLong(rs.getLong("problem_type_id")));
					//find all answer
					String query = "SELECT * FROM answers WHERE problem_id = ?";
					List<Map<String, Object>> rows = jdbcTemplate.queryForList(query, problemId);
					List<Answer> answers = new ArrayList<>();
					
					for(Map<String, Object> row : rows) {
						Answer a = new Answer();
						long id = new Long(((Integer)row.get("id")).intValue());
						long position = new Long(((Integer)row.get("position")).intValue());
						//only fills in id, value, is_correct, incorrect_message, problem_id, position
						a.setId(id);
						a.setValue((String)row.get("value"));
						a.setCorrect((boolean)row.get("is_correct"));
						a.setIncorrectMessage((String)row.get("incorrect_message"));
						a.setProblemId(problemId);
						a.setPositon(position);
						answers.add(a);
					}
					problem.setAnswers(answers);
					list.add(problem);
				}
				return null;
			}
		}, problemSetId);
		
		return list;
	}

	public ProblemSet find(long id){
		ProblemSet ps = new ProblemSet();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();

			String query = "SELECT * FROM sequences WHERE id = ?";
			PreparedStatement pstm = conn.prepareStatement(query);

			pstm.setLong(1, id);
			ResultSet rs = pstm.executeQuery();

			if (rs.next()) {
				ps.setDecodedID(rs.getInt("id"));
				ps.setName(rs.getString("name"));
				ps.setHeadSectionId(rs.getLong("head_section_id"));
				ps.setParameters(rs.getString("parameters"));
			} else {
				throw new RuntimeException(
						"Cannot find Problem Set with this " + id);
			}

			if (rs != null)
				rs.close();
			if (pstm != null)
				pstm.close();
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return ps;
	}
	
	public ProblemSet findByAssignment(String assignmentRef) throws ReferenceNotFoundException {
		
		//first get problem set id from assignment reference
		ExternalReferenceDao refDao = new JdbcExternalReferenceDao(ds);
		long id;
		try {
			id = refDao.getDbidFromReference(assignmentRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new ReferenceNotFoundException(e);
		}
		
		int problemSetId = jdbcTemplate.queryForObject("SELECT sequence_id FROM class_assignments WHERE id = ?", Integer.class, id);
		return find(problemSetId);
	}

	@Override
	public ProblemSection findBySectionId(long id) {
		ProblemSectionDAO psDao = new ProblemSectionDAO();

		return psDao.find(id);
	}

	@Override
	public List<FolderItem> getFolderItemsByFolder(long folderId) {
		List<FolderItem> items = new ArrayList<>();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String sql = "SELECT f.id AS folder_id, f.name AS folder_name, i.item_type AS type, i.position, s.name AS item_name, s.id AS problem_set_id "
					+ "FROM folders f "
					+ "RIGHT JOIN folder_items i ON f.id = i.item_id "
					+ "LEFT JOIN curriculum_items c ON c.id = i.item_id "
					+ "LEFT JOIN sequences s ON s.id = c.sequence_id "
					+ "WHERE i.folder_id = ? ORDER BY position DESC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, folderId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				String name = rs.getString("folder_name");
				if(rs.getString("type").equals("Folder")){
					FolderItem item = new FolderItem(ItemType.FOLDER, name);
					List<FolderItem> children = getFolderItemsByFolder(rs.getLong("folder_id"));
					item.setChildren(children);
					items.add(item);
				}else if(rs.getString("type").equals("CurriculumItem")){
					//the problem set
					long problemSetId = rs.getLong("problem_set_id");
					//get the share link ref by problemSetId in direct
					ProblemSet ps = find((int)problemSetId);

					FolderItem item = new FolderItem(ItemType.CURRICULUM_ITEM, name);
					item.setPs(ps);
					items.add(item);
				}
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			new RuntimeException(e);
		}
		return items;
	}
	
	@Override
	public JsonArray getProblemSetsByFolder(long folderId) {
		JsonArray problemSets = new JsonArray();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String sql = "SELECT f.id AS folder_id, f.name AS folder_name, i.item_type AS type, i.position, s.name AS item_name, s.id AS problem_set_id "
					+ "FROM folders f "
					+ "RIGHT JOIN folder_items i ON f.id = i.item_id "
					+ "LEFT JOIN curriculum_items c ON c.id = i.item_id "
					+ "LEFT JOIN sequences s ON s.id = c.sequence_id "
					+ "WHERE i.folder_id = ? ORDER BY position DESC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, folderId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				if(rs.getString("type").equals("Folder")){
					//the sub folder
					JsonObject folder = new JsonObject();
					folder.addProperty("type", "Folder");
					folder.addProperty("name", rs.getString("folder_name"));
					folder.add("problem_sets", getProblemSetsByFolder(rs.getLong("folder_id")));
					problemSets.add(folder);
					
				}else if(rs.getString("type").equals("CurriculumItem")){
					//the problem set
					long problemSetId = rs.getLong("problem_set_id");
					//get the share link ref by problemSetId in direct
					
					JsonObject problemSet = new JsonObject();
					problemSet.addProperty("id", Long.toString(problemSetId));
					problemSet.addProperty("type", "CurriculumItem");
					problemSet.addProperty("name", rs.getString("item_name"));
					problemSets.add(problemSet);
				}
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return problemSets;
	}

	@Override
	public List<Map<String, String>> getFoldersByIds(List<Integer> folderIdList) {
		List<Map<String, String>> folders = new ArrayList<Map<String, String>>();
		StringBuilder sb = new StringBuilder();
		for (int i=0; i<folderIdList.size();i++){
			sb.append("?,");
		}
		String s = sb.substring(0, sb.length()-1);
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String sql = "SELECT id, name FROM folders WHERE id IN ("+s+") ORDER BY name";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			for (int i=0;i<folderIdList.size();i++){
				pstmt.setLong(i+1, folderIdList.get(i));
			}
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				Map<String, String> folder = new HashMap<String, String>();
				folder.put("id", Long.toString(rs.getLong("id")));
				folder.put("name", rs.getString("name"));
				folders.add(folder);
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return folders;
	}

	@Override
	public List<Map<String, String>> getSubFoldersByFolderId(int folderId) {
		List<Map<String, String>> subFolderList = new ArrayList<Map<String, String>>();
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			String sql = "SELECT f.id AS folder_id, f.name AS folder_name, i.item_type AS type, i.position "
					+ "FROM folders f "
					+ "JOIN folder_items i ON f.id = i.item_id "
					+ "WHERE i.folder_id = ? ORDER BY position DESC";
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setLong(1, folderId);
			ResultSet rs = pstmt.executeQuery();
			while(rs.next()){
				if(rs.getString("type").equals("Folder")){
					//the sub folder
					Map<String, String> folder = new HashMap<String, String>();
					folder.put("id", String.valueOf(rs.getInt("folder_id")));
					folder.put("name", rs.getString("folder_name"));
					subFolderList.add(folder);
				}
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return subFolderList;
	}

}
