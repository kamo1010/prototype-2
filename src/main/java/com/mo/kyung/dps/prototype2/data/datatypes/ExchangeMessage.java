package com.mo.kyung.dps.prototype2.data.datatypes;

import java.util.Date;

import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.resources.ExchangeMessageReceiveResource;
import com.mo.kyung.dps.prototype2.data.resources.ExchangeMessageResource;

public class ExchangeMessage implements Comparable<ExchangeMessage>{
	private AccountUser author;
	private Topic topic;
	private String payload;
	private Date editionDate;
	public AccountUser getAuthor() {
		return author;
	}
	public void setAuthor(AccountUser author) {
		this.author = author;
	}
	public Topic getTopic() {
		return topic;
	}
	public void setTopic(Topic topicName) {
		this.topic = topicName;
	}
	public String getPayload() {
		return payload;
	}
	public void setPayload(String payload) {
		this.payload = payload;
	}
	public Date getEditionDate() {
		return editionDate;
	}
	public void setEditionDate(Date editionDate) {
		this.editionDate = editionDate;
	}
	public ExchangeMessage() {
		this.editionDate = new Date();
	}
	public ExchangeMessage(AccountUser author, Topic topic, String payload) {
		this.editionDate = new Date();
		this.author = author;
		this.topic = topic;
		this.payload = payload;
	}
	public ExchangeMessage(String author, ExchangeMessageResource message, Date editionDate) {
		this.editionDate = editionDate;
		this.author = Database.getUser(author);
		this.topic = Database.getTopic(message.getTopic());
		this.payload = message.getPayload();
	}
	public ExchangeMessageResource buildResource() {
		return new ExchangeMessageResource(topic.getName(), payload);
	}
	@Override
	public int compareTo(ExchangeMessage o) {
		return -1 * editionDate.compareTo(o.getEditionDate()); //anti-chronological order
	}
	public ExchangeMessageReceiveResource buildReceiveResource() {
		return new ExchangeMessageReceiveResource(new StringBuilder(author.getFirstName()).append(" ").append(author.getLastName()).toString(),
				topic.getName(), payload, editionDate.toString());
	}
}
