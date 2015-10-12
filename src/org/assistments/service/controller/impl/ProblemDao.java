package org.assistments.service.controller.impl;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.assistments.dao.ConnectionFactory;
import org.assistments.service.domain.Answer;
import org.assistments.service.domain.Problem;
import org.assistments.service.domain.Problem.Type;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;

public class ProblemDao {

	JdbcTemplate jdbc;
	
	public ProblemDao() {
		DataSource ds = ConnectionFactory.getDataSource();
		jdbc = new JdbcTemplate(ds);
	}
	
	public Problem findById(long problemId) {
		Problem problem = new Problem();
		String query = "SELECT * FROM problems WHERE id = ?";
		
		problem = jdbc.queryForObject(query, new RowMapper<Problem>() {
			@Override
			public Problem mapRow(ResultSet rs, int rowNum) throws SQLException {
				Problem problem = new Problem();
				//only fills in id, name, body, type id, assistment_id, position
				problem.setId(rs.getLong("id"));
				problem.setName(rs.getString("name"));
				problem.setBody(rs.getString("body"));
				problem.setAssistmentId(rs.getLong("assistment_id"));
				problem.setPosition(rs.getLong("position"));
				problem.setType(Type.fromLong(rs.getLong("problem_type_id")));
				return problem;
			}
		}, problemId);
		//get all answers
		query = "SELECT * FROM answers WHERE problem_id = ?";
		List<Map<String, Object>> rows = jdbc.queryForList(query, problemId);
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
		
		return problem;
	}
	
	public Problem findByAssistmentId(long assistmentId) {
		Problem problem = new Problem();
		//TODO: Change the query
		String query = "SELECT * FROM problems WHERE assistment_id = ? LIMIT 1";
		
		problem = jdbc.queryForObject(query, new RowMapper<Problem>() {
			@Override
			public Problem mapRow(ResultSet rs, int rowNum) throws SQLException {
				Problem problem = new Problem();
				//only fills in id, name, body, type id, assistment_id, position
				problem.setId(rs.getLong("id"));
				problem.setName(rs.getString("name"));
				problem.setBody(rs.getString("body"));
				problem.setAssistmentId(rs.getLong("assistment_id"));
				problem.setPosition(rs.getLong("position"));
				problem.setType(Type.fromLong(rs.getLong("problem_type_id")));
				return problem;
			}
		}, assistmentId);
		long problemId = problem.getId();
		//get all answers
		query = "SELECT * FROM answers WHERE problem_id = ?";
		List<Map<String, Object>> rows = jdbc.queryForList(query, problemId);
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
		
		return problem;
	}
}
