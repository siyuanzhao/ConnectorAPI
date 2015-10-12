package org.assistments.service.controller.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.assistments.dao.ConnectionFactory;
import org.assistments.service.domain.Answer;
import org.springframework.jdbc.core.JdbcTemplate;

public class AnswerControllerDAO {

	JdbcTemplate jdbc;
	
	public AnswerControllerDAO() {
		DataSource dataSource = ConnectionFactory.getDataSource();
		jdbc = new JdbcTemplate(dataSource);
	}
	
	public List<Answer> findByProblemId(long problemId) {
		List<Answer> list = new ArrayList<Answer>();
		
		String query = "SELECT * FROM answers WHERE problem_id = ?";
		
		List<Map<String, Object>> rows = jdbc.queryForList(query, problemId);
		for (Map<String, Object> row : rows) {
			Answer a = new Answer();
			long id = new Long(((Integer)row.get("id")).intValue()).longValue();
			problemId = new Long(((Integer)row.get("problem_id")).intValue()).longValue();
			long position = new Long(((Integer)row.get("position")).intValue()).longValue();
			a.setId(id);
			a.setValue((String)(row.get("value")));
			a.setCorrect((boolean)(row.get("is_correct")));
			a.setProblemId(problemId);
			a.setPositon(position);
			list.add(a);
		}
		return list;
	}
}
