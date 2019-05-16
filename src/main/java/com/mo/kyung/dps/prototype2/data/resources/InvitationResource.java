package com.mo.kyung.dps.prototype2.data.resources;

public class InvitationResource {
	private String topicName;
	private String login;
	public InvitationResource() {
	}
	public InvitationResource(String topicName, String login) {
		super();
		this.topicName = topicName;
		this.login = login;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
}
