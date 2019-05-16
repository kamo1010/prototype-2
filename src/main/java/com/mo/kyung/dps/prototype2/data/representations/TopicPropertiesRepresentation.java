package com.mo.kyung.dps.prototype2.data.representations;

import java.util.Collections;
import java.util.Set;

public class TopicPropertiesRepresentation {
	private String topicName;
	private boolean publik;
	private Set<String> subscribedUsersLogin;
	public TopicPropertiesRepresentation() {
		super();
	}
	public TopicPropertiesRepresentation(String topicName, boolean publik, Set<String> subscribedUsersLogin) {
		super();
		this.topicName = topicName;
		this.publik = publik;
		this.subscribedUsersLogin = subscribedUsersLogin;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public boolean isPublik() {
		return publik;
	}
	public void setPublik(boolean publik) {
		this.publik = publik;
	}
	public Set<String> getSubscribedUsersLogin() {
		return Collections.unmodifiableSet(subscribedUsersLogin);
	}
	public void setSubscribedUsersLogin(Set<String> subscribedUsersLogin) {
		this.subscribedUsersLogin = subscribedUsersLogin;
	}
}
