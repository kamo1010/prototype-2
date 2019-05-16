package com.mo.kyung.dps.prototype2.data.representations;

public class ExchangeMessageRepresentation {
	private String topic;
	private String payload;
	public ExchangeMessageRepresentation() {
		super();
	}
	public ExchangeMessageRepresentation(String topic, String payload) {
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
