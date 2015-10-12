package org.assistments.edmodo.utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class EdmodoHttpUtil {

	public static String sendURLGet(String fullURL) {
		String checkResponse = "";
		
		try {

			URL url = new URL(fullURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			
			connection.setRequestMethod("GET");

			BufferedReader in = new BufferedReader(new InputStreamReader(
					getInput(connection)));
			ApplicationSettings.setErrorStatusCode(connection.getResponseCode());
			
			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			checkResponse = response.toString();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return checkResponse;
	}

	public static String sendURLPost(String resourcesURL,String urlParameters) {
		String checkResponse = "";
		
		try {

			URL url = new URL(resourcesURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");

			connection.setRequestProperty("Content-Type",
					"application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setDoOutput(true);
			connection.setDoInput(true);
			
			connection.connect();
			
			OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
			writer.write(urlParameters);
			writer.flush();
	
			ApplicationSettings.setErrorStatusCode(connection.getResponseCode());
			
			//System.out.println(connection.getResponseCode());
			//System.out.println(connection.getResponseMessage());
			
			BufferedReader in = new BufferedReader(new InputStreamReader(
					getInput(connection)));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			checkResponse = response.toString();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
		return checkResponse;
	}
	
	private static InputStream getInput(HttpURLConnection connect) throws IOException {
		if(connect.getResponseCode() >= 400) {
			return connect.getErrorStream();
		} else {
			return connect.getInputStream();
		}
	}
}
