package org.assistments.edmodo.domain;

import java.util.List;

public class EdmodoGroup {

	private long group_id;
	private String title;
	private String member_count;
	private List<String> owners;
	private String subject;
	private String sub_subject;
	private String start_level;
	private String end_level;
	public long getGroup_id() {
		return group_id;
	}
	public void setGroup_id(long group_id) {
		this.group_id = group_id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getMember_count() {
		return member_count;
	}
	public void setMember_count(String member_count) {
		this.member_count = member_count;
	}
	public List<String> getOwners() {
		return owners;
	}
	public void setOwners(List<String> owners) {
		this.owners = owners;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSub_subject() {
		return sub_subject;
	}
	public void setSub_subject(String sub_subject) {
		this.sub_subject = sub_subject;
	}
	public String getStart_level() {
		return start_level;
	}
	public void setStart_level(String start_level) {
		this.start_level = start_level;
	}
	public String getEnd_level() {
		return end_level;
	}
	public void setEnd_level(String end_level) {
		this.end_level = end_level;
	}
	
	
}
