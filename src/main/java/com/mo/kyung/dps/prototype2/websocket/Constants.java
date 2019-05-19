package com.mo.kyung.dps.prototype2.websocket;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.datatypes.AccountUser;

public class Constants {
	private static final String INSTANTIATION_NOT_ALLOWED = "Instantiation not allowed";
	private static final String USER_NAME_KEY = "author";
	private static final String TOPIC_KEY = "topic";
	private static final String MESSAGE_KEY = "payload";
	private static final String EDITION_DATE_KEY = "editionDate";
	private static final ObjectMapper MAPPER = new ObjectMapper();
	private static final String ADMINISTRATION_TOPIC = "administration";
	private static final String CONNECTION_TOPIC = "connection";
	private static final String NEW_TOPIC_TOPIC = "new topic";
	private static final StringBuilder BUILDER = new StringBuilder();

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
	public static StringBuilder getBuilder(){
		return BUILDER;
	}
	public static String buildConnectedUsersAsString(){
		BUILDER.append("[");
		int iterator = 0;
		for (AccountUser user : Database.getConnectedUsers().values()) {
			BUILDER.append("\"").append(user.getLogin()).append("\"");
			if (iterator != Database.getConnectedUsers().size() - 1){
				BUILDER.append(", ");
			} else {
				iterator += 1;
			}
		}
		BUILDER.append("]");
		System.out.println(BUILDER.toString());
		return BUILDER.toString();
	}
}
