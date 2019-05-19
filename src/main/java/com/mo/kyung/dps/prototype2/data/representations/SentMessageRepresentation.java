package com.mo.kyung.dps.prototype2.data.representations;

/*
 * representation of messages resulting of actions done by the user
 */
public class SentMessageRepresentation {
	private String topic;
	private String payload;
	public SentMessageRepresentation() {
		super();
	}
	public SentMessageRepresentation(String topic, String payload) {
		this.topic = topic;
		this.payload = payload;
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
}
