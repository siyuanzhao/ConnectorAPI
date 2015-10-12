package org.assistments.edmodo.utility;

import java.util.List;

import com.google.gson.Gson;

public class EdmodoRequestGenerating {

	private String resources;
	private String api_key;
	private String access_token;
	
	protected EdmodoRequestGenerating() {
		
	}
	
	public static EdmodoRequestGenerating fromAppIdAccessToken(String appId, String accessToken) {
		EdmodoRequestGenerating generator = new EdmodoRequestGenerating();
		generator.api_key = ApplicationSettings.getAppApiKey(appId);
		generator.access_token = accessToken;
		return generator;
	}
	
	public static EdmodoRequestGenerating fromApiKeyAccessToken(String apiKey, String accessToken) {
		EdmodoRequestGenerating generator = new EdmodoRequestGenerating();
		generator.api_key = apiKey;
		generator.access_token = accessToken;
		return generator;
	}
	
	public static EdmodoRequestGenerating fromApiKey(String apiKey) {
		EdmodoRequestGenerating generator = new EdmodoRequestGenerating();
		generator.api_key = apiKey;
		
		return generator;
	}
	/*
	 * GET /profiles?api_key=<API_KEY>&access_token=<ACCESS_TOKEN>&user_tokens=["b020c42d1"]
	 */
	public String getProfile(List<String> userTokens) {
		
		String request = "";
		setResources("profiles");
		
		Gson gson = new Gson();
		String userTokensJson = gson.toJson(userTokens);
		
		request = "/" + getResources() + "?" + "api_key=" + getApi_key()+"&access_token="+getAccess_token()+"&user_tokens="+userTokensJson;
		request = ApplicationSettings.EdmodoAPIBase + request;
		return request;
	}
	
	//GET /users?api_key=<API_KEY>&access_token=<ACCESS_TOKEN>&user_tokens=["b020c42d1","jd3i1c0pl"]
			
	public String getUsers(List<String> userTokens) {
		
		String request = "";
		setResources("users");
		
		Gson gson = new Gson();
		String userTokensJson = gson.toJson(userTokens);
		
		request = "/" + getResources() + "?" + "api_key=" + getApi_key()+"&access_token="+getAccess_token()+"&user_tokens="+userTokensJson;
		request = ApplicationSettings.EdmodoAPIBase + request;
		
		return request;
	}
	
	//GET /members?api_key=<API_KEY>&access_token=<ACCESS_TOKEN>&group_id=379557,456322
	public String getMembers(List<Long> groups) {
		String request = "";
		
		setResources("members");
				
		Gson gson = new Gson();
		String groupJson = gson.toJson(groups);
		groupJson = groupJson.substring(1, groupJson.length()-1);
		//System.out.println(groupJson);
		
		request = "/" + getResources() + "?" + "api_key=" + getApi_key()+"&access_token="+getAccess_token()+"&group_id="+groupJson;
		request = ApplicationSettings.EdmodoAPIBase + request;
		
		
		return request;
	}
	
	//GET /groups?api_key=<API_KEY>&access_token=<ACCESS_TOKEN>&group_ids=[379557,379562]
	public String getGroupsInfo(List<Long> groups) {
		String request = "";
		
		setResources("groups");
		Gson gson = new Gson();
		String groupJson = gson.toJson(groups);
		//System.out.println(groupJson);
		
		request = "/" + getResources() + "?" + "api_key=" + getApi_key()+"&access_token="+getAccess_token()+"&group_ids="+groupJson;
		request = ApplicationSettings.EdmodoAPIBase + request;
		
		return request;
		
	}
	
	// GET /groupsForUser?api_key=<API_KEY>&access_token=<ACCESS_TOKEN>&user_token=b020c42d1
	public String getGroupsForUser(String userToken) {
		return String.format("%s/groupsForUser?api_key=%s&access_token=%s&user_token=%s", ApplicationSettings.EdmodoAPIBase, getApi_key(), getAccess_token(), userToken);
	}
	
	public String launchRequest(String launchKey) {

		String request = "";
		setResources("launchRequests");
					
		request = "/" + getResources() + "?" + "api_key=" + getApi_key()+"&launch_key="+ launchKey;
		request = ApplicationSettings.EdmodoAPIBase + request;

		return request;
	}
	
	public String createAssignmentURL() {
		String request = "";
		setResources("createAssignment");
		
		request="/"+getResources();
		request = ApplicationSettings.EdmodoAPIBase + request;
		return request;
	}
	
	//"api_key=6394d0cc748116b42de5dfea25d4b36fc10d3024
	//&access_token=atok_mEU80wgr8mXvRgI5uw29&user_token=56265b05d
	//&title=Numeral+to+words&description=An+assingment+from+ASSISTments+app.+Please+click+on+Launch+Edmodo+App+to+do+the+assignment.&due_date=2014-12-30&recipients=[{\"group_id\":584315}]&attachments=[{\"type\":\"link\",\"title\":\"Numeral to words 2.NBT.A.3\",\"url\":\"app://\"}]";
	//dueDate is in the format of yyyy-MM-dd
	public String createAssignmentParameters(Long group,String user_token,String edmodo_access_token, 
			String title, String assignmentRef, String dueDate) {
		String request = "";
		title = title.replaceAll(" ", "+");
//		String dueDate = DateUtil.addOneYear();
		request += "api_key="+getApi_key()+"&access_token="+edmodo_access_token+
				"&user_token="+user_token+"&title="+title+
				"&description=ASSISTments&due_date="+dueDate+"&";
		request +="recipients=[{\"group_id\":"+group+"}]" + 
				"&attachments=[{\"type\":\"link\",\"title\":\"" + title +"\",\"url\":\"app://?assignment_ref="+assignmentRef+"\"}]";

		return request;
	}
	
	public String createTurninURL() {
		String request = "";
		setResources("turnInAssignment");
		
		request="/"+getResources();
		request = ApplicationSettings.EdmodoAPIBase + request;
		return request;
	}
	
	public String turnInAssignment(String user_token, String edmodo_access_token, 
			String edmodo_assingment_id ) {
		String request ="";
		request += "api_key="+getApi_key()+"&access_token="+
				edmodo_access_token+"&user_token="+user_token+"&assignment_id="+
				edmodo_assingment_id+"&content="+ApplicationSettings.edmodo_turnin_assingment_text;
		return request;
	}
	
	
	public String setGradeURL() {
		String request = "";
		setResources("setGrade");
		
		request="/"+getResources();
		request = ApplicationSettings.EdmodoAPIBase + request;
		return request;
	}
	
	public String setGrade(String user_token, String edmodo_access_token, String edmodo_assingment_id, String score, String total ) {
		String request ="";
		request += "api_key="+getApi_key()+"&access_token="+edmodo_access_token+"&user_token="+user_token+"&assignment_id="+edmodo_assingment_id+"&score="+score+"&total="+total;
		return request;
	}
	
	public String getResources() {
		return resources;
	}
	public void setResources(String resources) {
		this.resources = resources;
	}
	public String getApi_key() {
		return api_key;
	}
	public void setApi_key(String api_key) {
		this.api_key = api_key;
	}
	public String getAccess_token() {
		return access_token;
	}
	public void setAccess_token(String access_token) {
		this.access_token = access_token;
	}
	
	
}
