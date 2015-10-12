package org.assistments.service.domain;

import java.util.ArrayList;
import java.util.List;

public class Folder {

	long id;
	String name;
	
	List<FolderItem> children;
	
	public Folder() {
		children = new ArrayList<>();
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

	public List<FolderItem> getChildren() {
		return children;
	}

	public void setChildren(List<FolderItem> children) {
		this.children = children;
	}
}
