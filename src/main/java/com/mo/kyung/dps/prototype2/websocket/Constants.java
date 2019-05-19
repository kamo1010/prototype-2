package com.mo.kyung.dps.prototype2.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;

public class Constants {
	private static final String INSTANTIATION_NOT_ALLOWED = "Instantiation not allowed";
	private static final String USER_NAME_KEY = "author";
	private static final String TOPIC_KEY = "topic";
	private static final String MESSAGE_KEY = "payload";
	private static final String EDITION_DATE_KEY = "editionDate";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String ADMINISTRATION_TOPIC = "Administration";
	private static final String CONNECTION_TOPIC = "Connection";
	private static final String NEW_TOPIC_TOPIC = "New Topic";

    public static String getAdministrationTopic() {
		return ADMINISTRATION_TOPIC;
	}
	public static String getConnectionTopic() {
		return CONNECTION_TOPIC;
	}
	public static String getNewTopicTopic() {
		return NEW_TOPIC_TOPIC;
	}
	private Constants() {
        throw new IllegalStateException(INSTANTIATION_NOT_ALLOWED);
    }
	public static String getInstantiationNotAllowed() {
		return INSTANTIATION_NOT_ALLOWED;
	}
	public static String getUserNameKey() {
		return USER_NAME_KEY;
	}
	public static String getMessageKey() {
		return MESSAGE_KEY;
	}
	public static String getTopicKey() {
		return TOPIC_KEY;
	}
	public static String getEditionDateKey() {
		return EDITION_DATE_KEY;
	}
	public static ObjectMapper getMapper() {
		return MAPPER;
	}
}
