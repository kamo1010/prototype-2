package com.mo.kyung.dps.prototype2.data.datatypes;

import java.util.Date;

public class ExchangeMessage implements Comparable<ExchangeMessage>{
	private AccountUser login;
	private Topic topic;
	private String payload;
	private Date editionDate;
	public ExchangeMessage() {
		this.editionDate = new Date();
	}
	public ExchangeMessage(AccountUser user, Topic topic, String payload) {
		this.editionDate = new Date();
		this.login = user;
		this.topic = topic;
		this.payload = payload;
	}
	public ExchangeMessage(AccountUser user, Topic topic, String payload, Date date) {
		this.editionDate = date;
		this.login = user;
		this.topic = topic;
		this.payload = payload;
	}
	public AccountUser getUser() {
		return login;
	}
	public Topic getTopic() {
		return topic;
	}
	public String getPayload() {
		return payload;
	}
	public Date getEditionDate() {
		return editionDate;
	}
	@Override
	public int compareTo(ExchangeMessage o) {
		return -1 * editionDate.compareTo(o.getEditionDate()); //anti-chronological order
	}
}
