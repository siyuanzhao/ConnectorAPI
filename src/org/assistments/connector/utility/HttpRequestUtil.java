package org.assistments.connector.utility;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public class HttpRequestUtil {
	public static final String CONTENT_TYPE_JSON = "application/json";
	public static final String partner_reference = "Hien-Ref";
	public static final String ASSITments_Auth_WOBehalf = "partner=" + "\"" + partner_reference + "\"";

	public static Response sendPostRequest(String fullURL, String payLoad, String partnerRef) {

		try {
			URL url = new URL(fullURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setRequestMethod("POST");

			// below is the header
			String auth = "partner=" + "\"" + partnerRef + "\"";
			connection.setRequestProperty("assistments-auth",
					auth);
			connection.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);

			connection.setDoOutput(true);
			connection.connect();

			OutputStream os = connection.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
			pw.write(payLoad);
			pw.close();

			//ApplicationSettings.setErrorStatusCode(connection.getResponseCode());
			
			BufferedReader in = null;
			if (connection.getResponseCode() >= 400) {
				in = new BufferedReader(new InputStreamReader(
						connection.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
			}

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			
			Response res = new Response(connection.getResponseCode(), response.toString());
			return res;

		} catch (Exception e) {
			System.out.println("An error might occur with sendPostWOBehalf: "
					+ fullURL);
			e.printStackTrace();
			Response res = new Response(500, "An error might occur with sendPostWOBehalf: "
					+ fullURL);
			//ApplicationSettings.setErrorTitle("Error while communicating with Edmodo. We are sorry cannot proceed further!");
			//ApplicationSettings.setErrorDetail("An error might occur with sendPostWOBehalf: "+ fullURL);
			//TODO: Should not return null
			return res;
		}
	}

	public static Response sendGetRequest(String fullURL, String partnerRef)  {

//		String responseJson = "";
//		BufferedReader reader = null;

		try {
			URL url = new URL(fullURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("GET");

			// below is the header
			String auth = "partner=" + "\"" + partnerRef + "\"";
			connection.setRequestProperty("assistments-auth",
					auth);
			connection.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);

			//ApplicationSettings.setErrorStatusCode(connection.getResponseCode());

			BufferedReader in = null;
			if (connection.getResponseCode() >= 400) {
				in = new BufferedReader(new InputStreamReader(
						connection.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
			}

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			System.out.println(response.toString());

			//responseJson = response.toString();
			Response res = new Response(connection.getResponseCode(), response.toString());
			return res;

		} catch (Exception e) {
			System.out.println("An error might occur with sendGetWOBehalf: "
					+ fullURL);
			e.printStackTrace();
			
			Response res = new Response(500, "An error might occur with sendGetWOBehalf: "
					+ fullURL);
//			ApplicationSettings.setErrorTitle("Error while communicating with Edmodo. We are sorry cannot proceed further!");
//			ApplicationSettings.setErrorDetail("An error might occur with sendGetWOBehalf: "+ fullURL);
			//TODO: Should not return null
			return res;
		}
	}

	//Partner Reference is not needed.
	public static Response sendGetText(String fullURL)  {

		String responseText = "";
//		BufferedReader reader = null;

		try {
			URL url = new URL(fullURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			// text--> no need header
			connection.setRequestMethod("GET");

			BufferedReader in = null;
			if (connection.getResponseCode() >= 400) {
				in = new BufferedReader(new InputStreamReader(
						connection.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
			}

//			ApplicationSettings.setErrorStatusCode(connection.getResponseCode());

			String inputLine;
			StringBuffer response = new StringBuffer();
			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();
			// System.out.println(response.toString());
			responseText = response.toString();
			Response res = new Response(connection.getResponseCode(), responseText);
			return res;

		} catch (Exception e) {
			System.out
					.println("An error might occur with sendGetText: "
							+ fullURL);
			Response res = new Response(500, "An error might occur with sendGetText: "
					+ fullURL);
			
			e.printStackTrace();
//			ApplicationSettings.setErrorTitle("Error while communicating with Edmodo. We are sorry cannot proceed further!");
//			ApplicationSettings.setErrorDetail("An error might occur with sendGetTextWOBehalf: "+ fullURL);
			return res;
		}
	}

	public static Response sendPostRequest(String fullURL, String payload, String partnerRef, String onBehalf) {

		try {
			URL url = new URL(fullURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("POST");

			String withBehalf = "partner=" + "\""
					+ partnerRef + "\",onBehalfOf="
					+ "\"" + onBehalf + "\"";
			connection.setRequestProperty("assistments-auth", withBehalf);
			connection.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);

			connection.setDoOutput(true);

			connection.connect();

			OutputStream os = connection.getOutputStream();
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(os));
			pw.write(payload);
			pw.close();

//			ApplicationSettings.setErrorStatusCode(connection.getResponseCode());
			// System.out.println(ApplicationSettings.getErrorStatusCode() + " "
			// + " ------" );

			BufferedReader in = null;
			if (connection.getResponseCode() >= 400) {
				in = new BufferedReader(new InputStreamReader(
						connection.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
			}

			String inputLine;
			StringBuffer content = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

			if (content.toString().equals("")) {
//				responseJson = ApplicationSettings.getErrorStatusCode().toString();
			}

			// print result
			// System.out.println(response.toString());

//			responseJson = response.toString();
			Response res = new Response(connection.getResponseCode(), content.toString());
			return res;

		} catch (Exception e) {
			System.out.println("An error might occur with sendPostBehalf: "
					+ fullURL);
			e.printStackTrace();
			Response res = new Response(500, "An error might occur with sendPostBehalf: "
					+ fullURL);
//			ApplicationSettings.setErrorTitle("Error while communicating with Edmodo. We are sorry cannot proceed further!");
//			ApplicationSettings.setErrorDetail("An error might occur with sendPostBehalf: "+ fullURL);
			//TODO: Should not return null
			return res;
		}
	}

	public static Response sendGetRequest(String fullURL, String partnerRef, String onBehalf) {
		Response res = null;
		try {
			URL url = new URL(fullURL);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();

			connection.setRequestMethod("GET");

			String withBehalf = "partner=" + "\""
					+ partnerRef + "\",onBehalfOf="
					+ "\"" + onBehalf + "\"";
			connection.setRequestProperty("assistments-auth", withBehalf);
			connection.setRequestProperty("Content-Type", CONTENT_TYPE_JSON);

			connection.connect();

			//ApplicationSettings
				//	.setErrorStatusCode(connection.getResponseCode());
			// System.out.println(ApplicationSettings.getErrorStatusCode() + " "
			// + " ------" );

			BufferedReader in = null;
			if (connection.getResponseCode() >= 400) {
				in = new BufferedReader(new InputStreamReader(
						connection.getErrorStream()));
			} else {
				in = new BufferedReader(new InputStreamReader(
						connection.getInputStream()));
			}

			String inputLine;
			StringBuffer content = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				content.append(inputLine);
			}
			in.close();

			// print result
			// System.out.println(response.toString());
			res = new Response(connection.getResponseCode(), content.toString());
		} catch (Exception e) {
			e.printStackTrace();
			res = new Response(500, "There is an error when calling sendGetRequest");
		}
		return res;
	}
}
