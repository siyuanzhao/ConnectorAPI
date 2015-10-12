package org.assistments.edmodo.controller;

import org.assistments.edmodo.utility.EdmodoHttpUtil;
import org.assistments.edmodo.utility.EdmodoRequestGenerating;
import org.assistments.service.controller.ErrorLogController;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class EdmodoAssignmentController {
	
	private String user_token;
	private String apiKey;
	private String access_token;
	EdmodoRequestGenerating generator;
	
	public EdmodoAssignmentController(String apiKey, String userToken, String accessToken) {
		this.user_token = userToken;
		this.apiKey = apiKey;
		this.access_token = accessToken;
		generator = EdmodoRequestGenerating.fromApiKeyAccessToken(this.apiKey, accessToken);
	}
	
	//create assignment for each group
	public String createGroupAssignment(long group_id, String title, String assignmentRef, String dueDate) {
		String edmodoAssignmentId = "";
		
		String resources = generator.createAssignmentURL();
		String parameters = generator.createAssignmentParameters(group_id, user_token, access_token, 
				title, assignmentRef, dueDate);
		
		//send request to Edmodo to create the assignment
		String json = EdmodoHttpUtil.sendURLPost(resources, parameters);
		edmodoAssignmentId = parseAssignmentJson(json);

		return edmodoAssignmentId;
	}
	
	public void turnInAssignment(String assignmentId) {
		String resources = generator.createTurninURL();
		
		String parameters = generator.turnInAssignment(user_token, access_token, assignmentId);
		EdmodoHttpUtil.sendURLPost(resources, parameters);
		
	}
	
	public void setGrade(String assignmentId, String score, String total) {
		String resources = generator.setGradeURL();
		
		String params = generator.setGrade(user_token, access_token, assignmentId, score, total);
		
		EdmodoHttpUtil.sendURLPost(resources, params);
	}
	
	private String parseAssignmentJson(String json) {
		JsonElement jEelement = new JsonParser().parse(json);
	    JsonObject jObject = jEelement.getAsJsonObject(); //"assignment_id":7693626
	    String assignmentId = new String();
	    try {
	    	assignmentId = jObject.get("assignment_id").toString(); //7693626
	    } catch(Exception e) {
	    	e = new Exception(json);
	    	ErrorLogController.addNewError(e, "Connector");
	    }
	    return assignmentId;
	}
}
