package org.assistments.service.controller.impl;

import java.util.HashMap;
import java.util.Map;

import org.assistments.connector.exception.WebAPIMalfunctionException;
import org.assistments.connector.utility.Constants;
import org.assistments.connector.utility.HttpRequestUtil;
import org.assistments.connector.utility.Response;
import org.assistments.service.controller.SchoolController;

import com.google.gson.Gson;

public class SchoolControllerWebImpl implements SchoolController {
	
	String partnerRef;
	String onBehalf;
	
	public SchoolControllerWebImpl(String partnerRef, String onBehalf) {
		this.partnerRef = partnerRef;
		this.onBehalf = onBehalf;
	}

	
	public static Response createSchool(String NCES, String partnerRef) {
		String requestURL = Constants.API_BASE + "/school";
		String payload = new String();
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("nces", NCES);
		Gson gson = new Gson();
		payload = gson.toJson(m);
		
		Response r = HttpRequestUtil.sendPostRequest(requestURL, payload, partnerRef);
		return r;
	}
	
	@Override
	public boolean assignUserToSchool(String userRef, String schoolRef) 
			throws WebAPIMalfunctionException {
		boolean b = false;
		String requestURL = Constants.API_BASE + "/school_membership";
		
		Map<String, String> m = new HashMap<String, String>();
		m.put("user", userRef);
		m.put("school", schoolRef);
		
		Gson gson = new Gson();
		String payload = gson.toJson(m);
		
		Response r = HttpRequestUtil.sendPostRequest(requestURL, payload, partnerRef, onBehalf);
		if(r.getHttpCode() == 201) {
			b = true;
		} else {
			throw new WebAPIMalfunctionException(r.getContent());
		}
		return b;
	}
	
	public static Response getSchoolByNCES(String nces, String partnerRef) {
		String apiGetSchool = String.format("%1$s/school?nces=%2$s", Constants.API_BASE, nces);
		
		Response res = HttpRequestUtil.sendGetRequest(apiGetSchool, partnerRef);
		
		return res;
	}
}
