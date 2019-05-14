package com.mo.kyung.dps.prototype2.data.datatypes;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.mo.kyung.dps.prototype2.data.resources.UserDetailResource;

public class AccountUser {
	private String firstName, lastName, login, password;
	private Set<Topic> topics;
	private String token;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Set<Topic> getTopics() {
		return Collections.unmodifiableSet(topics);
	}
	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}
	private boolean addTopic(Topic topic) {
		return this.topics.add(topic);
	}
	private boolean removeTopic(Topic topic) {
		return this.topics.remove(topic);
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public boolean isConnected() {
		if (token.isEmpty()) {
			return true;
		} return false;
	}
	public AccountUser() {
		super();
	}
	public AccountUser(String firstName, String lastName, String login, String password) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.password = password;
		this.topics = new HashSet<Topic>();
		this.token = null;
	}
	public boolean isInterestedIn(Topic topic) {
		return topics.contains(topic);
	}
	public UserDetailResource buildUserDetailResource() {
		return new UserDetailResource(firstName, lastName, login, topics);
	}
	public boolean subscribeToTopic(Topic topic) {
		return (addTopic(topic) && topic.addSubscriber(this));
	}
	public boolean unsubscribe(Topic topic) {
		return removeTopic(topic);
	}
}
