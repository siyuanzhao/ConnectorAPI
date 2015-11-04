package org.assistments.service.controller.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.sql.DataSource;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.exception.WebAPIMalfunctionException;
import org.assistments.connector.utility.Constants;
import org.assistments.connector.utility.HttpRequestUtil;
import org.assistments.connector.utility.Response;
import org.assistments.dao.ConnectionFactory;
import org.assistments.domain.Assignment;
import org.assistments.service.controller.AssignmentController;
import org.assistments.service.dao.AssignmentDao;
import org.assistments.service.dao.AssignmentNotFoundException;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.ExternalReferenceNotFoundException;
import org.assistments.service.dao.jdbc.JdbcAssignmentDao;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcFolderUtil;
import org.assistments.util.Util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AssignmentControllerWebImpl implements AssignmentController {

	String partnerRef;
	String onBehalf;
	ExternalReferenceDao refDao;
	AssignmentDao assignmentDao;
	
	public AssignmentControllerWebImpl(String partnerRef, String onBehalf) {
		this.partnerRef = partnerRef;
		this.onBehalf = onBehalf;
		DataSource dataSource = ConnectionFactory.getDataSource();
		refDao = new JdbcExternalReferenceDao(dataSource);
		JdbcFolderUtil folderUtil = new JdbcFolderUtil(dataSource);
		assignmentDao = new JdbcAssignmentDao(dataSource, folderUtil);
	}
	
	@Override
	public String createAssignment(String problemSetId, String classRef) 
			throws WebAPIMalfunctionException{
		String payload = "{" + "\"" + "problemSet" + "\"" + ":" + "\""
				+ problemSetId + "\"" + "," + "\"" + "class" + "\"" + ":"
				+ "\"" + classRef + "\"" + "," + "\"" + "scope" + "\"" + ":" + "\""
				+ classRef + "\"" + "}";
		
		String requestURL = Constants.API_BASE + "/assignment";
		String assignmentRef = new String();
		
		Response r = HttpRequestUtil.sendPostRequest(requestURL, payload, partnerRef, onBehalf);
		if(r.getHttpCode() == 201) {
			assignmentRef = parseAssignmentJson(r.getContent());
		} else {
			throw new WebAPIMalfunctionException(r.getContent());
		}
		return assignmentRef;
	}
	
	public static Response getGrade(String userRef, String assignmentRef, String partnerID) {
		String getScoreURL = Constants.GET_SCORE_URL;
		String fullURL = getScoreURL+"?user_ref="+userRef+"&assignment_ref="+assignmentRef+"&partner_id="+partnerID;
		
		Response res = HttpRequestUtil.sendGetText(fullURL);
		return res;
	}
	
	@Override
	public String getAssignment(String assignmentRef, String onExit) 
			throws WebAPIMalfunctionException {
		String tutorURL = new String();
		String requestURL = Constants.API_BASE + "/assignment/" + assignmentRef + "?onExit="+onExit;
		
		Response r = HttpRequestUtil.sendGetRequest(requestURL, partnerRef, onBehalf);
		if(r.getHttpCode() == 200) {
			tutorURL = AssignmentControllerWebImpl.parseTutorJson(r.getContent());
		} else {
			throw new WebAPIMalfunctionException(r.getContent());
		}
		
		return tutorURL;
	}
	
	public static String parseAssignmentJson(String json) {
		JsonElement jEelement = new JsonParser().parse(json);
		JsonObject jObject = jEelement.getAsJsonObject();
		String assignmentRef = jObject.get("assignment").getAsString();
		
		return assignmentRef;
	}
	
	public static String parseTutorJson(String json) {
		JsonElement jEelement = new JsonParser().parse(json);
		JsonObject jObject = jEelement.getAsJsonObject();
		String url = jObject.get("handler").getAsString();
		
		return url;
	}

	@Override
	public String getAssignment(String assignmentRef, String onExit, String logoUrl, String whiteLabeled,
			String accountName) {
		String url;
		try {
			url = getAssignment(assignmentRef, onExit);
		} catch (WebAPIMalfunctionException e) {
			throw new RuntimeException();
		}
		StringBuilder sb = new StringBuilder();
		if(!Util.isNullOrEmpty(logoUrl)) {
			sb.append("&logoUrl="+logoUrl);
		}
		if(!Util.isNullOrEmpty(whiteLabeled)) {
			sb.append("&whiteLabeled="+whiteLabeled);
		}
		if(!Util.isNullOrEmpty(accountName)) {
			sb.append("&accountName="+accountName);
		}
		if(!("").equals(sb.toString())) {
			String append = sb.toString();
			try {
				append = URLEncoder.encode(append, "UTF-8");
				append = URLEncoder.encode(append, "UTF-8");
			} catch (UnsupportedEncodingException e) {
				throw new RuntimeException();
			}
			url += append;
		}
		return url;
	}

	@Override
	public Assignment find(String assignmentRef) throws ReferenceNotFoundException {
		long id;
		try {
			id = refDao.getDbidFromReference(assignmentRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new ReferenceNotFoundException(e);
		}
		try {
			return assignmentDao.findClassAssignmentById(id);
		} catch (AssignmentNotFoundException e) {
			throw new ReferenceNotFoundException(e);
		}
	}
}
