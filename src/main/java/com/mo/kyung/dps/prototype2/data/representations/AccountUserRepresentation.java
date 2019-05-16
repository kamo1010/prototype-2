package com.mo.kyung.dps.prototype2.data.representations;

public class AccountUserRepresentation {
	private String login;
	private String password;
	public AccountUserRepresentation() {
		super();
	}
	public AccountUserRepresentation(String login, String password) {
		super();
		this.login = login;
		this.password = password;
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
}