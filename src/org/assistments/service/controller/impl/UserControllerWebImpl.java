package org.assistments.service.controller.impl;

import java.net.URLEncoder;

import org.assistments.connector.exception.WebAPIMalfunctionException;
import org.assistments.connector.utility.Constants;
import org.assistments.connector.utility.HttpRequestUtil;
import org.assistments.connector.utility.Response;
import org.assistments.service.controller.UserController;
import org.assistments.service.domain.User;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class UserControllerWebImpl implements UserController{
	
	String partnerRef;
	String accessToken;
	
	public UserControllerWebImpl(String partnerRef, String accessToken) {
		this.partnerRef = partnerRef;
		this.accessToken = accessToken;
	}
	
	@Override
	public User getUserProfile(String userRef) 
			throws WebAPIMalfunctionException {		
		String url = Constants.API_BASE + "/user/" + userRef;
		
		Response r = HttpRequestUtil.sendGetRequest(url, partnerRef, accessToken);

		User user = new User();
		if(r.getHttpCode() == 200) { //success
			JsonElement jEelement = new JsonParser().parse(r.getContent());
			JsonObject jObject = jEelement.getAsJsonObject();
			String userType = jObject.get("userType").getAsString();
			String firstName = jObject.get("firstName").getAsString();
			String lastName = jObject.get("lastName").getAsString();
			String displayName = jObject.get("displayName").getAsString();
			String userName = jObject.get("username").getAsString();
			String stuEmail = jObject.get("email").isJsonNull() ? "" :  jObject.get("email").getAsString();
			String timeZone = jObject.get("timezone").isJsonNull() ? "" : jObject.get("timeZone").getAsString();
			user.setUserType(userType);
			user.setFirstName(firstName);
			user.setLastName(lastName);
			user.setDisplayName(displayName);
			user.setUsername(userName);
			user.setEmail(stuEmail);
			user.setTimeZone(timeZone);
		} else {
			throw new WebAPIMalfunctionException(r.getContent());
		}
		
		return user;
	}

	public static Response linkUserByEmail(String email) {
		String url = Constants.ASSISSTments_URL + "api2_helper/link_user_by_email?email="+email;
		Response r = HttpRequestUtil.sendGetText(url);
		return r;
	}
	
	@Deprecated
	public static String getUserPageURL(String userType, String partnerRef, String onBehalf, String appId) {
		
		String addressToGo = "";
		
		String onFailure = "www.google.com";
		String onExit = Constants.CONNECTOR_URL+"/gradeRequest";
		
		if (userType.equals(Constants.TEACHER_ROLE)) {
			String onSuccess = Constants.TEACHER_PAGE+"/"+appId;
			addressToGo = String.format("%1$s?partner=%2$s&access=%3$s&on_success=%4$s&on_failure=%5$s", Constants.LOGIN_URL, partnerRef, onBehalf, onSuccess, onFailure);
		}else if (userType.equals(Constants.STUDENT_ROLE)) {
			//TODO: partner_id should not be here
			String onSuccess = Constants.STUDENT_PAGE+"/"+appId+"?onExit="+onExit+"&partner_id=3";
			try {
				onSuccess = URLEncoder.encode(onSuccess, "UTF-8");
			} catch (Exception e) {
				e.printStackTrace();
			}
 			addressToGo = String.format("%1$s?partner=%2$s&access=%3$s&on_success=%4$s&on_failure=%5$s", Constants.LOGIN_URL, partnerRef, onBehalf, onSuccess, onFailure);
		}
		return addressToGo;
	}
}
