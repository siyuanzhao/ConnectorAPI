package org.assistments.service.domain;

import java.util.ArrayList;
import java.util.List;

public class ProblemSection {
	
	public enum Type {
		ProblemSection("ProblemSection"),
		LinearSection("LinearSection"),
		IfThenElseSection("IfThenElseSection"),
		ChooseConditionSection("ChooseConditionSection"),
		MasterySection("MasterySection"),
		RandomChildOrderSection("RandomChildOrderSection"),
		LinearMasterySection("LinearMasterySection");
		
		String name;
		
		Type(String name) {
			this.name = name;
		}
		
		public String getName() {
			return name;
		}
		
		public static Type fromString(String name) {
			if(name != null) {
				for(Type t : Type.values()) {
					if(name.equalsIgnoreCase(t.name)) {
						return t;
					}
				}
			}
			//fail to get Type, throw exception
			throw new IllegalArgumentException("No Type with name = " + name + " found");
		}
	}
	long id;
	Type type;
	long problemSetId;
	String name;
	List<ProblemSection> children;
	List<Problem> problems; //This list only contains the problems directly under this section.
	
	public ProblemSection() {
		children = new ArrayList<>();
		problems = new ArrayList<>();
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public Type getType() {
		return type;
	}
	public void setType(Type type) {
		this.type = type;
	}
	public long getProblemSetId() {
		return problemSetId;
	}
	public void setProblemSetId(long problemSetId) {
		this.problemSetId = problemSetId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public List<ProblemSection> getChildren() {
		return children;
	}
	public void setChildren(List<ProblemSection> children) {
		this.children = children;
	}
	public List<Problem> getProblems() {
		return problems;
	}
	public void setProblems(List<Problem> problems) {
		this.problems = problems;
	}
}
