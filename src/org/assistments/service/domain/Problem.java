package org.assistments.service.domain;

import java.util.ArrayList;
import java.util.List;

public class Problem {
	
	public enum Type {
		Choose1 (1), ChooseN (2), Rank(3), FillIn1(4), Algebra(5),
		OpenResponse(8), External(9), Racket(10), FillInIgnoreCase(11);
		
		long typeId;
		
		Type(long typeId) {
			this.typeId = typeId;
		}
		
		public long getTypeId() {
			return typeId;
		}
		
		public static Type fromLong(long typeId) {
			if(typeId > 0) {
				for(Type t : Type.values()) {
					if(t.getTypeId() == typeId) {
						return t;
					}
				}
			}
			throw new IllegalArgumentException("Cannot find problem type with type id = ? " + typeId);
		}
	}
	long id;
	String name;
	String body;
	Type type;
	long assistmentId;
	long scaffoldId;
	long position;
	List<Answer> answers;
	
	public Problem() {
		answers = new ArrayList<>();
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public long getAssistmentId() {
		return assistmentId;
	}

	public void setAssistmentId(long assistmentId) {
		this.assistmentId = assistmentId;
	}

	public long getScaffoldId() {
		return scaffoldId;
	}

	public void setScaffoldId(long scaffoldId) {
		this.scaffoldId = scaffoldId;
	}

	public long getPosition() {
		return position;
	}

	public void setPosition(long position) {
		this.position = position;
	}

	public List<Answer> getAnswers() {
		return answers;
	}

	public void setAnswers(List<Answer> answers) {
		this.answers = answers;
	}
	
	
}
