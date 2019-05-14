package com.mo.kyung.dps.prototype2.data.resources;

import java.util.Set;

import com.mo.kyung.dps.prototype2.data.datatypes.Topic;

public class UserDetailResource {
	private String firstName, lastName, login;
	private Set<Topic> topics;
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
	public Set<Topic> getTopics() {
		return topics;
	}
	public void setTopics(Set<Topic> topics) {
		this.topics = topics;
	}
	public UserDetailResource() {
		super();
	}
	public UserDetailResource(String firstName, String lastName, String login, Set<Topic> topics) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.topics = topics;
	}
}
