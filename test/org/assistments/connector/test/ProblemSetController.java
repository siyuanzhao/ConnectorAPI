package org.assistments.connector.test;

import org.assistments.service.controller.impl.ProblemSectionDAO;
import org.assistments.service.domain.ProblemSection;

public class ProblemSetController {

	
	public static void main(String[] args) {
		ProblemSectionDAO dao = new ProblemSectionDAO();
		ProblemSection section = dao.find(3807404);
		System.out.println(section.getName());
		for(ProblemSection ps : section.getChildren()) {
			System.out.println(ps.getName());
		}
	}
}
