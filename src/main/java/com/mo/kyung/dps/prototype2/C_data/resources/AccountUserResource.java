package com.mo.kyung.dps.prototype2.C_data.resources;

public class AccountUserResource {
	private String login;
	private String password;
	public AccountUserResource(String login, String password) {
		super();
		this.login = login;
		this.password = password;
	}
	public AccountUserResource() {
		super();
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