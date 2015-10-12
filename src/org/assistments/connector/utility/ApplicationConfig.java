package org.assistments.connector.utility;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class ApplicationConfig {

	static ApplicationContext ct = null;
	
	public static void loadSpringConfig() {
		if(ct == null) {
			ct = new ClassPathXmlApplicationContext("/connector.xml");
		}
	}
}
