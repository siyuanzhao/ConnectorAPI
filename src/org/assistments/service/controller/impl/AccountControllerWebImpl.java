package org.assistments.service.controller.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import org.assistments.connector.exception.ReferenceNotFoundException;
import org.assistments.connector.exception.WebAPIMalfunctionException;
import org.assistments.connector.utility.Constants;
import org.assistments.connector.utility.HttpRequestUtil;
import org.assistments.connector.utility.Response;
import org.assistments.dao.ConnectionFactory;
import org.assistments.service.controller.AccountController;
import org.assistments.service.dao.ExternalReferenceDao;
import org.assistments.service.dao.jdbc.JdbcExternalReferenceDao;
import org.assistments.service.domain.User;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AccountControllerWebImpl implements AccountController {
	
	String apiPartnerRef;
	ExternalReferenceDao refDao;

	public AccountControllerWebImpl(String apiPartnerRef) {
		this.apiPartnerRef = apiPartnerRef;
		DataSource dataSource = ConnectionFactory.getDataSource();
		refDao = new JdbcExternalReferenceDao(dataSource);
	}

	public User getDistributorInfo(long  id) throws ReferenceNotFoundException, SQLException {
		User user = new User();
		
		String query = "SELECT * FROM users u JOIN user_details ud ON ud.user_id = u.id WHERE u.id = ?";
		
		Connection conn = ConnectionFactory.getInstance().getConnection();
		PreparedStatement pstm = conn.prepareStatement(query);
		pstm.setLong(1, id);
		ResultSet rs = pstm.executeQuery();
		
		if(rs.next()) {
			user.setFirstName(rs.getString("first_name"));
			user.setLastName(rs.getString("last_name"));
			user.setEmail(rs.getString("email"));
			user.setDisplayName(rs.getString("display_name"));
		} else {
			//throws an exception
			throw new ReferenceNotFoundException("Cannot find user with this id!");
		}
		if(rs != null) 
			rs.close();
		if(pstm != null)
			pstm.close();
		if(conn != null) 
			conn.close();
		return user;
	}
	
	@Override
	public String createUser(User user)
			throws WebAPIMalfunctionException {
			Gson gson = new Gson();
			String payload = gson.toJson(user);
			String userRef = new String();
			
			String requestURL = String.format("%1$s/user", Constants.API_BASE);
			Response r = HttpRequestUtil.sendPostRequest(requestURL, payload, apiPartnerRef);
			
			if(r.getHttpCode() == 201) {
				JsonElement jElement = new JsonParser().parse(r.getContent());
				JsonObject jObject = jElement.getAsJsonObject();
				userRef = jObject.get("user").getAsString();
			} else {
				throw new WebAPIMalfunctionException(r.getContent());
			}
			
			return userRef;
		}
		
		@Override
		public String createAccessToken(String userRef) 
				throws WebAPIMalfunctionException {
			
			String requestURL = Constants.API_BASE + "/access_token";
			
			Map<String, String> m = new HashMap<String, String>();
			m.put("user", userRef);
			
			Gson gson = new Gson();
			String payload = gson.toJson(m);
			String accessToken = new String();
			
			Response r = HttpRequestUtil.sendPostRequest(requestURL, payload, apiPartnerRef);
			
			if(r.getHttpCode() == 200) {
				JsonElement jElement = new JsonParser().parse(r.getContent());
				JsonObject jObject = jElement.getAsJsonObject();
				accessToken = jObject.get("access").getAsString();
			} else {
				throw new WebAPIMalfunctionException(r.getContent());
			}
			return accessToken;
		}

	@Override
	public boolean isEmailInUse(String email) 
			throws WebAPIMalfunctionException {
		boolean result = false;
		
		String url = Constants.API_BASE + "/user?email="+email;

		Response r = HttpRequestUtil.sendGetRequest(url, apiPartnerRef);
		if(r.getHttpCode() == 200) {
			result = true;
		} else if(r.getHttpCode() == 404) {
			result = false;
		} else {
			throw new WebAPIMalfunctionException(r.getContent());
		}
		return result;
	}

	@Override
	public User getUserProfile(String userRef) throws Exception{
		long id = refDao.getDbidFromReference(userRef);
//		long id = Long.valueOf(userRef);
		User user = getDistributorInfo(id);
		return user;
	}
	
	@Override
	public User getUserProfileById(long id){
//		long id = Long.valueOf(userRef);
		User user;
		try {
			user = getDistributorInfo(id);
		} catch (ReferenceNotFoundException e) {
			throw new RuntimeException(e);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return user;
	}

	@Override
	public boolean isTeacherExist(String email) {
		String sql = "SELECT COUNT(*) FROM users WHERE email = ?";
		try {
			Connection conn = ConnectionFactory.getInstance().getConnection();
			PreparedStatement pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, email);
			ResultSet rs = pstmt.executeQuery();
			if(rs.next()){
				if(rs.getInt("count") > 0) return true;
			}
			rs.close();
			pstmt.close();
			conn.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;
	}
	
}
