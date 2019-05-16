package com.mo.kyung.dps.prototype2.C_data.a_resources;

import java.util.Set;

public class UserDetailResource {
	private String firstName, lastName, login;
	private Set<String> topicsNames;
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstNames) {
		this.firstName = firstNames;
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
	public Set<String> getTopicsNames() {
		return topicsNames;
	}
	public void setTopics(Set<String> topicsNames) {
		this.topicsNames = topicsNames;
	}
	public UserDetailResource() {
		super();
	}
	public UserDetailResource(String firstName, String lastName, String login, Set<String> topicsNames) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.login = login;
		this.topicsNames = topicsNames;
	}
}
