package org.assistments.service.controller.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.assistments.connector.exception.WebAPIMalfunctionException;
import org.assistments.connector.utility.Constants;
import org.assistments.connector.utility.HttpRequestUtil;
import org.assistments.connector.utility.Response;
import org.assistments.dao.ConnectionFactory;
import org.assistments.service.controller.StudentClassController;
import org.assistments.service.dao.ClassDao;
import org.assistments.service.dao.ClassMembershipDao;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcClassDao;
import org.assistments.service.dao.jdbc.JdbcClassMembershipDao;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcFolderUtil;
import org.assistments.service.dao.ExternalReferenceNotFoundException;
import org.assistments.service.domain.StudentClass;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class StudentClassControllerWebImpl implements StudentClassController {
	
	String partnerRef;
	String accessToken;
	
	ClassDao classDao;
	ExternalReferenceDao refDao;
	ClassMembershipDao memberDao;

	public StudentClassControllerWebImpl(String partnerRef, String accessToken) {
		this.partnerRef = partnerRef;
		this.accessToken = accessToken;
		DataSource dataSource = ConnectionFactory.getDataSource();
		this.refDao = new JdbcExternalReferenceDao(dataSource);
		this.memberDao = new JdbcClassMembershipDao(dataSource);
		this.classDao = new JdbcClassDao(dataSource, new JdbcFolderUtil(dataSource));
	}

	public String createStudentClass(StudentClass studentClass
			) throws WebAPIMalfunctionException {

		String requestURL = Constants.API_BASE + "/student_class";

		Gson gson = new Gson();
		String payload = gson.toJson(studentClass);

		Response r = HttpRequestUtil.sendPostRequest(requestURL, payload,
				partnerRef, accessToken);
		
		String stuClassRef = new String();
		if(r.getHttpCode() == 201) {
			stuClassRef = parseClassJson(r.getContent());
		} else {
			throw new WebAPIMalfunctionException(r.getContent());
		}
		return stuClassRef;

	}

	public boolean enrollStudent(String classRef, String studentRef
			) throws WebAPIMalfunctionException {
		boolean result = false;
		Map<String, String> m = new HashMap<String, String>();

		m.put("user", studentRef);
		m.put("class", classRef);

		Gson gson = new Gson();
		String payload = gson.toJson(m);

		String requestURL = Constants.API_BASE + "/class_membership";
		Response r = HttpRequestUtil.sendPostRequest(requestURL, payload,
				partnerRef, accessToken);
		
		if(r.getHttpCode() == 200) {
			result = true;
		} else if (r.getHttpCode() == 401) {
			throw new WebAPIMalfunctionException(r.getContent());
		}

		return result;
	}

	// get all class refs which a student is in
	public Response getAllClasses(String userRef, String onBehalf) {
		String url = Constants.API_BASE
				+ "/class_membership?student=" + userRef;
		Response res = null;
		res = HttpRequestUtil.sendGetRequest(url, partnerRef, onBehalf);

		return res;
	}
	
	@Override
	public List<String> getClassMembership(String studentClassRef) {
		List<String> list = new ArrayList<String>();
		
		String url = Constants.API_BASE + "/class_membership?class=" + studentClassRef;
		Response res = HttpRequestUtil.sendGetRequest(url, partnerRef, accessToken);
		
		if(res.getHttpCode() == 200) {
			//parse the response to get each student ref
			JsonElement jEelement = new JsonParser().parse(res.getContent());
			JsonObject jObject = jEelement.getAsJsonObject();
			JsonArray jsonStuRefArr = jObject.get("students").getAsJsonArray();
			Iterator<JsonElement> iter = jsonStuRefArr.iterator();
			while(iter.hasNext()) {
				JsonElement tmpElement = iter.next();
				list.add(tmpElement.getAsString());
			}
		} else {
			new RuntimeException(res.getContent());
		}
		
		return list;
	}
	
	private String parseClassJson(String classRefJson) {
		JsonElement jEelement = new JsonParser().parse(classRefJson);
		JsonObject jObject = jEelement.getAsJsonObject();
		String classRef = jObject.get("class").getAsString();
		return classRef;
	}

	@Override
	public String createClassSection(String studentClassRef, String sectionName) {
		long classId;
		try {
			classId = refDao.getDbidFromReference(studentClassRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
//		long sectionId = classDao.createClassSection(classId, sectionName);
//		String sectionRef = refDao.createExternalReference(Table.STUDENT_CLASS_SECTIONS, partnerRef, sectionId);
//		return sectionRef;
		return null;
	}

	@Override
	public void updateSectionName(String sectionRef, String sectionName) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void changeStudentSection(String userRef, String oldSectionRef, String newSectionRef) {
		long userId = 0;
		long oldSectionId = 0;
		long newSectionId = 0;
		try {
			userId = refDao.getDbidFromReference(userRef);
			oldSectionId = refDao.getDbidFromReference(oldSectionRef);
			newSectionId = refDao.getDbidFromReference(newSectionRef);
		} catch(ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
//		memberDao.updateMembership(userId, oldSectionId, newSectionId);
	}

	@Override
	public List<String> getClassSections(String studentClassRef) {
		List<String> list = new ArrayList<String>();
		//get student class id
		long classId;
		try {
			classId = refDao.getDbidFromReference(studentClassRef);
		} catch (ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
		List<Long> sectionIds = classDao.getSectionIdsForClass(classId);
		Iterator<Long> iter = sectionIds.iterator();
		while(iter.hasNext()) {
			Long id = iter.next();
			String ref = refDao.getReferenceForEntity("student_class_section", partnerRef, id);
			list.add(ref);
		}
		return list;
	}

	@Override
	public String getSectionName(String sectionRef) {
		long sectionId;
		try {
			sectionId = refDao.getDbidFromReference(sectionRef);
		} catch(ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
//		String sectionName = classDao.getSectionName(sectionId);
//		return sectionName;		
		return null;
	}

	@Override
	public String getClassForSection(String sectionRef) {
		long sectionId;
		try {
			sectionId = refDao.getDbidFromReference(sectionRef);
		} catch(ExternalReferenceNotFoundException e) {
			throw new RuntimeException(e);
		}
		org.assistments.domain.StudentClass sc = classDao.findBySectionId(sectionId);
		long classId = sc.getId();
		String classRef = refDao.getReferenceForEntity("student_classes", 
				partnerRef, classId);
		return classRef;
	}
}
