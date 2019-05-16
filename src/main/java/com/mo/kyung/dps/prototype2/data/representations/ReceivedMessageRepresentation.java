package com.mo.kyung.dps.prototype2.data.representations;

public class ReceivedMessageRepresentation {
	private String author;
	private String topic;
	private String payload;
	private String editionDate;
	public ReceivedMessageRepresentation() {
		super();
	}
	public ReceivedMessageRepresentation(String author, String topic, String payload, String editionDate) {
		super();
		this.author = author;
		this.topic = topic;
		this.payload = payload;
		this.editionDate = editionDate;
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
