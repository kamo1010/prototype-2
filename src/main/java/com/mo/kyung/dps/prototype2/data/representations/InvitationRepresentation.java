package com.mo.kyung.dps.prototype2.data.representations;

public class InvitationRepresentation {
	private String topicName;
	private String login;
	public InvitationRepresentation() {
	}
	public InvitationRepresentation(String topicName, String login) {
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
