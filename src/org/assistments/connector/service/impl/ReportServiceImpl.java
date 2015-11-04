package org.assistments.connector.service.impl;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.assistments.connector.service.ReportService;
import org.assistments.dao.ConnectionFactory;
import org.assistments.domain.ProblemLog;
import org.assistments.service.controller.impl.ProblemDao;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.ExternalReferenceNotFoundException;
import org.assistments.service.dao.ProblemLogDao;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcPoblemLogDao;
import org.assistments.service.domain.Problem;
import org.assistments.service.domain.StudentReportEntry;
import org.springframework.jdbc.core.JdbcTemplate;

public class ReportServiceImpl implements ReportService {
	
	JdbcTemplate jdbcTemplate;
	DataSource ds;
	ExternalReferenceDao refDao;
	ProblemLogDao logDao;
	
	public ReportServiceImpl() {
		ds = ConnectionFactory.getDataSource();
		jdbcTemplate = new JdbcTemplate(ds);
		refDao = new JdbcExternalReferenceDao(ds);
		logDao = new JdbcPoblemLogDao(ds);
	}
	
	public List<StudentReportEntry> generateStudentReport(String studentRef, String assignmentRef) {
		long studentId = 0;
		long assignmentId = 0;
		try {
			studentId = refDao.getDbidFromReference(studentRef);
			assignmentId = refDao.getDbidFromReference(assignmentRef);
		} catch (ExternalReferenceNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DecimalFormat df = new DecimalFormat("#%");
		List<StudentReportEntry> entryList = new ArrayList<>();
		//get all problem logs
		//sort the problem log based on proble log id
		final List<ProblemLog> logList = logDao.findByUserandAssignment(studentId, assignmentId);
		
		Collections.sort(logList, new Comparator<ProblemLog>() {

			@Override
			public int compare(ProblemLog log1, ProblemLog log2) {
				return new Long(log1.getId() - log2.getId()).intValue();
			}
		});
		ProblemDao problemDao = new ProblemDao();
		Map<Long, Double> averageMap = new HashMap<>();
		Iterator<ProblemLog> iter = logList.iterator();
		while(iter.hasNext()) {
			StudentReportEntry entry = new StudentReportEntry();
			
			ProblemLog log = iter.next();
			long problemId = log.getProblemId();
			Problem problem = problemDao.findById(problemId);
			entry.setDecodedProblemId(problem.getId());
			entry.setProblemBody(problem.getBody());
			if(log.getAnswerText() != null) {
				entry.setMyAnswer(log.getAnswerText());
			} 
			if(log.getCorrect() != null) {
				entry.setCorrect(log.getCorrect());
				String correctPercent = df.format(log.getCorrect());
				entry.setCorrectPercent(correctPercent);
			} else {
				entry.setCorrectPercent("Ungraded");
			}
			
			//check if the assignment is finished
			if(log.getEndTime() == null) {
				entry.setCompleted(false);
			} else {
				entry.setCompleted(true);
			}
			
			entry.setHintUsage(log.getHintCount());
			entry.setTeacherComment(log.getTeacherComment());
			
			entryList.add(entry);
			
			if(log.getCorrect() != null) {
				//calculate class average
				if(averageMap.containsKey(problemId)) {
					double average = averageMap.get(problemId);
					average = (average+log.getCorrect())/(averageMap.size()+1);
					//update the map
					averageMap.put(problemId, average);
				} else {
					averageMap.put(problemId, log.getCorrect());
				}
			}
		}
		
		//add class average to report entry
		Iterator<StudentReportEntry> iter1 = entryList.iterator();
		while(iter1.hasNext()) {
			StudentReportEntry entry = iter1.next();
			
			long problemId = entry.getDecodedProblemId();
			if(averageMap.containsKey(problemId)) {
				double average = averageMap.get(problemId);
				entry.setClassAverage(df.format(average));
			} else {
				entry.setClassAverage("N/A");
			}
		}
		
		return entryList;
	}

}
