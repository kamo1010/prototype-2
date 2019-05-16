package com.mo.kyung.dps.prototype2.data.representations;

public class TopicCreationRepresentation {
	private String topicName;
	private boolean publik;
	public TopicCreationRepresentation() {
		super();
	}
	public TopicCreationRepresentation(String topicName, boolean publik) {
		super();
		this.topicName = topicName;
		this.publik = publik;
	}
	public String getTopicName() {
		return topicName;
	}
	public void setTopicName(String topicName) {
		this.topicName = topicName;
	}
	public boolean isPublik() {
		return publik;
	}
	public void setPublik(boolean publik) {
		this.publik = publik;
	}
}
