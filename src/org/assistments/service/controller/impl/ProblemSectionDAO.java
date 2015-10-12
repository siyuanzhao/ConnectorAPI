package org.assistments.service.controller.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.assistments.dao.ConnectionFactory;
import org.assistments.service.domain.Problem;
import org.assistments.service.domain.ProblemSection;
import org.assistments.service.domain.ProblemSection.Type;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ProblemSectionDAO {
	
	JdbcTemplate jdbc;
	
	public ProblemSectionDAO() {
		DataSource ds = ConnectionFactory.getDataSource();
		jdbc = new JdbcTemplate(ds);
	}
	
	//recursively to get problem section structure
	public ProblemSection find(long problemSectionId) {
//		List<ProblemSection> list = new ArrayList<ProblemSection>();
		
		//first find the section from table sections 
		String query = "SELECT * FROM sections WHERE id = ?";
		
		ProblemSection section = jdbc.queryForObject(query, new RowMapper<ProblemSection>() {
			@Override
			public ProblemSection mapRow(ResultSet rs, int rowNum) throws SQLException {
				//only fill in id, type, sequence id, name
				ProblemSection section = new ProblemSection();
				section.setId(rs.getLong("id"));
				section.setType(Type.fromString(rs.getString("type")));
				section.setProblemSetId(rs.getLong("sequence_id"));
				section.setName(rs.getString("name"));
				return section;
			}
		}, problemSectionId);
		
		//check section type
		switch(section.getType()) {
		case ProblemSection:
			//doesn't need to get problems
			//get direct problem children
			query = "SELECT * FROM assistment_to_sequence_associations WHERE section_id = ?";
			List<Map<String, Object>> rows = jdbc.queryForList(query, section.getId());
			ProblemDao problemDao = new ProblemDao();
			List<Problem> problems = new ArrayList<>();
			for(Map<String, Object> row : rows) {
				long assistmentId = new Long(((Integer)row.get("assistment_id")).intValue());
				//find problem
				Problem tmpProblem = problemDao.findByAssistmentId(assistmentId);
				problems.add(tmpProblem);
			}
			section.setProblems(problems);
			break;
		default:
			break;
		}
		
		//get direct section children for this section
		query = "SELECT child_id FROM section_links WHERE parent_id = ? order BY position ASC";
		List<Map<String, Object>> rows = jdbc.queryForList(query, section.getId());
		if(rows.isEmpty()) { //There is no children section for this section
			return section;
		} else {
			//populate children section
			List<ProblemSection> children = new ArrayList<>();
			for(Map<String, Object> row : rows) {
				long id = new Long(((Integer)row.get("child_id")).intValue());
				ProblemSection tmpSection = find(id);
				children.add(tmpSection);
			}
			section.setChildren(children);
		}
		
		return section;
	}

}
