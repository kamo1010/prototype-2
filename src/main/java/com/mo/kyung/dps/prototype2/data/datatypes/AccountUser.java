package com.mo.kyung.dps.prototype2.data.datatypes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class AccountUser implements Comparable<AccountUser> {
	private String firstName, lastName, login, password;
	private Set<Topic> topics;
	private String token;
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
		this.token = "";
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getLogin() {
		return login;
	}
	private void setLogin(String login) {
		this.login = login;
	}
	public String getPassword() {
		return password;
	}
	private void setPassword(String password) {
		this.password = password;
	}
	public Set<Topic> getTopics() {
		return Collections.unmodifiableSet(topics);
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
	@Override
	public int compareTo(AccountUser anotherUser) {
		return getLogin().compareTo(anotherUser.getLogin());
	}
	public void changeLogin(String newLogin) {
		setLogin(newLogin);
	}
	public void changePassword(String newPassword) {
		setPassword(newPassword);
	}
	public void logIn() throws UnsupportedEncodingException {
		if (!isConnected()) {
			//encode "login@101@password" and set it as token
			token = Base64.getEncoder().encodeToString(new StringBuilder(login).append("@101@").append(password).toString().getBytes(StandardCharsets.UTF_8.toString()));
		}
	}
	public void logOut() {
		if (isConnected()) {
			token="";
		}
	}
	public boolean isConnected() {
		return !token.isEmpty();
	}
	public boolean isInterestedIn(Topic topic) {
		return topics.contains(topic);
	}
	public boolean subscribeToTopic(Topic topic) {
		return (addTopic(topic) && topic.addSubscriber(this));
	}
	public boolean unsubscribe(Topic topic) {
		return (removeTopic(topic) && topic.removeSubscriber(this));
	}
}
