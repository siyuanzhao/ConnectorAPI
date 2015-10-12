package org.assistments.service.controller;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Calendar;

import org.assistments.connector.utility.Utils;
import org.assistments.dao.ConnectionFactory;

public class ErrorLogController {

	public static boolean addNewError(String sourceFile, String sourceType, String errorType, 
			String stackTrace, int lineNum, long userId, String message, Timestamp time) {
		boolean b = false;
		//convert null to empty string
		sourceFile = Utils.convertNullToEmptyString(sourceFile);
		sourceType = Utils.convertNullToEmptyString(sourceType);
		errorType = Utils.convertNullToEmptyString(errorType);
		stackTrace = Utils.convertNullToEmptyString(stackTrace);
		message = Utils.convertNullToEmptyString(message);
		
		String query = "INSERT INTO error_logs (source_file, source_type, "
				+ "error_type, stack_trace, line_number, user_id, message, created_at) "
				+ "VALUES (?,?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, sourceFile);
			pstm.setString(2, sourceType);
			pstm.setString(3, errorType);
			pstm.setString(4, stackTrace);
			pstm.setInt(5, lineNum);
			pstm.setLong(6, userId);
			pstm.setString(7, message);
			pstm.setTimestamp(8, time);
			int affectedRows = pstm.executeUpdate();
			if(affectedRows != 0) {
				b = true;
			} else {
				b = false;
			}
			pstm.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstm != null) {
				try {
					pstm.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return b;
	}
	
	public static void addNewError(String sourceFile, String sourceType, String errorType, 
			String stackTrace, int lineNum, String message, Timestamp time) {

		//convert null to empty string
		sourceFile = Utils.convertNullToEmptyString(sourceFile);
		sourceType = Utils.convertNullToEmptyString(sourceType);
		errorType = Utils.convertNullToEmptyString(errorType);
		stackTrace = Utils.convertNullToEmptyString(stackTrace);
		message = Utils.convertNullToEmptyString(message);
		
		String query = "INSERT INTO error_logs (source_file, source_type, "
				+ "error_type, stack_trace, line_number, message, created_at) "
				+ "VALUES (?,?,?,?,?,?,?)";
		Connection conn = null;
		PreparedStatement pstm = null;
		try {
			conn = ConnectionFactory.getInstance().getConnection();
			pstm = conn.prepareStatement(query);
			pstm.setString(1, sourceFile);
			pstm.setString(2, sourceType);
			pstm.setString(3, errorType);
			pstm.setString(4, stackTrace);
			pstm.setInt(5, lineNum);
//			pstm.setLong(6, userId);
			pstm.setString(6, message);
			pstm.setTimestamp(7, time);
			pstm.executeUpdate();
			
			pstm.close();
			conn.close();
		} catch(SQLException e) {
			e.printStackTrace();
		} finally {
			if(pstm != null) {
				try {
					pstm.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
			if(conn != null) {
				try {
					conn.close();
				} catch(SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void addNewError(Throwable t, String sourceType) {
		StackTraceElement st = t.getStackTrace()[0];
		String sourceFile = st.getClassName();
		String errorType = st.getClass().getName();
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		String stackTrace = sw.getBuffer().toString();
		String message = t.getMessage();
		int lineNum = st.getLineNumber();
		Calendar c = Calendar.getInstance();
		Timestamp now = new Timestamp(c.getTimeInMillis());
		addNewError(sourceFile, sourceType, errorType, stackTrace, lineNum, message, now);
	}
	
	public static void addNewError(Throwable t, long userId, String sourceType) {
		StackTraceElement st = t.getStackTrace()[0];
		String sourceFile = st.getClassName();
		String errorType = st.getClass().getName();
		final StringWriter sw = new StringWriter();
		final PrintWriter pw = new PrintWriter(sw, true);
		t.printStackTrace(pw);
		String stackTrace = sw.getBuffer().toString();
		String message = t.getMessage();
		int lineNum = st.getLineNumber();
		Calendar c = Calendar.getInstance();
		Timestamp now = new Timestamp(c.getTimeInMillis());
		//TODO: userId is set 1 only because error log will show up properly on web page.
		addNewError(sourceFile, sourceType, errorType, stackTrace, lineNum, userId, message, now);
	}
}
