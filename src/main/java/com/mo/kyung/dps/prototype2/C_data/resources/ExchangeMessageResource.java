package com.mo.kyung.dps.prototype2.C_data.resources;

public class ExchangeMessageResource {
	private String topic;
	private String payload;

	public ExchangeMessageResource(String topic, String payload) {
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
	public ExchangeMessageResource() {
		super();
	}
}
