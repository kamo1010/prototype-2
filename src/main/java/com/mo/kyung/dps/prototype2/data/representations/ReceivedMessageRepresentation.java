package com.mo.kyung.dps.prototype2.data.representations;

import java.util.Date;

import com.mo.kyung.dps.prototype2.data.datatypes.AccountUser;
import com.mo.kyung.dps.prototype2.data.datatypes.Topic;

public class ReceivedMessageRepresentation {
	private String author;
	private String topic;
	private String payload;
	private String editionDate;
	public ReceivedMessageRepresentation() {
		super();
	}
	public ReceivedMessageRepresentation(AccountUser accountUser, Topic topic, String payload, Date date) {
		super();
		this.author = accountUser.getLogin();
		this.topic = topic.getName();
		this.payload = payload;
		this.editionDate = date.toString();
	}
	public String getAuthor() {
		return author;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public String getTopic() {
		return topic;
	}
	public void setTopic(String topic) {
		this.topic = topic;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public String getEditionDate() {
		return editionDate;
	}
	public void setEditionDate(String editionDate) {
		this.editionDate = editionDate;
	}
}
