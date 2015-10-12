package org.assistments.edmodo.controller;

import org.assistments.edmodo.domain.EdmodoLaunchRequest;
import org.assistments.edmodo.utility.EdmodoHttpUtil;
import org.assistments.edmodo.utility.EdmodoRequestGenerating;

import com.google.gson.Gson;

public class EdmodoLaunchController {
	EdmodoRequestGenerating generator;
	
	public EdmodoLaunchController(String apiKey) {
		generator = EdmodoRequestGenerating.fromApiKey(apiKey);
	}
	public EdmodoLaunchRequest launchRequest(String launchKey) {
		
		String url = generator.launchRequest(launchKey);
		String respJson = EdmodoHttpUtil.sendURLGet(url);

		Gson gson = new Gson();
		return gson.fromJson(respJson, EdmodoLaunchRequest.class);
	}
}
