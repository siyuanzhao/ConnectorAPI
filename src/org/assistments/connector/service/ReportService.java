package org.assistments.connector.service;

import java.util.List;

import org.assistments.service.domain.StudentReportEntry;

public interface ReportService {

	public List<StudentReportEntry> generateStudentReport(String studentRef, String assignmentRef);
}
