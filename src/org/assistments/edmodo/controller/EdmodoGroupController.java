package org.assistments.edmodo.controller;

import java.util.ArrayList;
import java.util.List;

import org.assistments.edmodo.domain.EdmodoGroup;
import org.assistments.edmodo.utility.EdmodoHttpUtil;
import org.assistments.edmodo.utility.EdmodoRequestGenerating;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EdmodoGroupController {
	
	private String apiKey;
	private String accessToken;
	private EdmodoRequestGenerating generator;
	
	public EdmodoGroupController(String apiKey, String accessToken) {
		this.apiKey = apiKey;
		this.accessToken = accessToken;
		generator = EdmodoRequestGenerating.fromApiKeyAccessToken(this.apiKey, this.accessToken);
	}
	
	public List<EdmodoGroup> getGroups(List<Long> groupIds) {
		List<EdmodoGroup> result = new ArrayList<>();
		
		String url = generator.getGroupsInfo(groupIds);
		String respJson = EdmodoHttpUtil.sendURLGet(url);
		Gson gson = new Gson();
		
		result = gson.fromJson(respJson, new TypeToken<List<EdmodoGroup>>(){}.getType());
		return result;
	}
	
	public List<EdmodoGroup> getGroupForUser(String userToken) {
		List<EdmodoGroup> result = new ArrayList<>();
		
		String url = generator.getGroupsForUser(userToken);
		
		String respJson = EdmodoHttpUtil.sendURLGet(url);
		Gson  gson = new Gson();
		
		result = gson.fromJson(respJson, new TypeToken<List<EdmodoGroup>>(){}.getType());
		return result;
	}
	
}
