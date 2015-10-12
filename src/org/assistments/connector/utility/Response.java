package org.assistments.connector.utility;

public class Response {
	int httpCode;
	String content;
	
	public Response(int httpCode, String content) {
		this.httpCode = httpCode;
		this.content = content;
	}
	
	public int getHttpCode() {
		return this.httpCode;
	}
	public String getContent() {
		return this.content;
	}
}

