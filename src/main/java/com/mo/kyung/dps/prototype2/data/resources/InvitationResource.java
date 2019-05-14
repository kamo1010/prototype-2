package com.mo.kyung.dps.prototype2.data.resources;

import java.util.ArrayList;
import java.util.List;

public class InvitationResource {
	private String topicName;
	private List<String> logins;
	public InvitationResource() {
		super();
		this.logins = new ArrayList<String>();
	}
	public InvitationResource(String topicName, List<String> logins) {
		super();
		this.topicName = topicName;
		this.logins = logins;
	}
	public List<String> getLogins() {
		return logins;
	}
	public void setLogins(List<String> logins) {
		this.logins = logins;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}
