package com.mo.kyung.dps.prototype2.data.datatypes;

import java.util.Date;

public class ExchangeMessage implements Comparable<ExchangeMessage>{
	private AccountUser author;
	private Topic topic;
	private String payload;
	private Date editionDate;
	public ExchangeMessage() {
		this.editionDate = new Date();
	}
	public ExchangeMessage(AccountUser author, Topic topic, String payload) {
		this.editionDate = new Date();
		this.author = author;
		this.topic = topic;
		this.payload = payload;
	}
	public AccountUser getAuthor() {
		return author;
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
