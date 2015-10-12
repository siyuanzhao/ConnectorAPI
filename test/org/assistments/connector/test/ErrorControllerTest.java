package org.assistments.connector.test;

import org.assistments.service.controller.ErrorLogController;

public class ErrorControllerTest {

	public static void main(String[] args) {
		ErrorLogController.addNewError(new RuntimeException(), "Direct");
	}
}
