package org.assistments.connector.test;

import java.util.List;

import org.assistments.edmodo.domain.EdmodoGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class EdmodoJsonTest {

	public static void main(String[] args) {
		groupsForUser();
	}
	
	public static void groupsForUser() {
		String json = "[ {\"group_id\":379557,\"title\":\"Algebra\","
				+ "\"member_count\":20, \"owners\":[\"b020c42d1\",\"693d5c765\"],"
				+ "\"start_level\":\"9th\",\"end_level\":\"9th\"},"
				+ "{\"group_id\":379562,\"title\":\"Geometry\",\"member_count\":28,"
				+ "\"owners\":[\"b020c42d1\"],\"start_level\":\"3rd\",\"end_level\":\"3rd\"}]";
		
		Gson gson = new Gson();
		
		List<EdmodoGroup> result = gson.fromJson(json, new TypeToken<List<EdmodoGroup>>(){}.getType());
		System.out.println(result.get(0).getOwners().get(1));
		System.out.println(result.get(0).getGroup_id());
	}
}
