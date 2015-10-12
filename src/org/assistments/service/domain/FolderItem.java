package org.assistments.service.domain;

import java.util.List;

public class FolderItem {

	ItemType type;
	String name;
	
	List<FolderItem> children;
	ProblemSet ps;	
	
	public enum ItemType {
		FOLDER("folder"),
		CURRICULUM_ITEM("CurriculumItem");
		
		String typeName;
		ItemType(String typeName) {
			this.typeName = typeName;
		}
		
		String getTypeName() {
			return this.typeName;
		}
	}
	
	public FolderItem(ItemType type, String name) {
		this.type = type;
		this.name = name;
	}

	public ItemType getType() {
		return type;
	}

	public void setType(ItemType type) {
		this.type = type;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<FolderItem> getChildren() {
		return children;
	}

	public void setChildren(List<FolderItem> children) {
		this.children = children;
	}

	public ProblemSet getPs() {
		return ps;
	}

	public void setPs(ProblemSet ps) {
		this.ps = ps;
	}
}
