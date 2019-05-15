package com.mo.kyung.dps.prototype2.C_data;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.mo.kyung.dps.prototype2.C_data.datatypes.AccountUser;
import com.mo.kyung.dps.prototype2.C_data.datatypes.ExchangeMessage;
import com.mo.kyung.dps.prototype2.C_data.datatypes.Topic;

public class Database {
	private static Map<String, AccountUser> users = new TreeMap<String, AccountUser>();
	private static Map<String, Topic> topics = new TreeMap<String, Topic>();
	private static Set<ExchangeMessage> uploadedMessages = new TreeSet<ExchangeMessage>();
	
	static {
		addUser(new AccountUser("Patrick", "GRIMBERG", "patrick", "grimberg"));
		addUser(new AccountUser("Jérôme", "DAZIANO", "jerome", "daziano"));
		addUser(new AccountUser("Maud", "CADORET", "maud", "cadoret"));
		addUser(new AccountUser("Hamza", "OUCHEIKH", "hamza", "oucheikh"));
		addUser(new AccountUser("Abdoulaye", "SOW", "abdoulaye", "sow"));
		addUser(new AccountUser("Pascal", "REINA", "pascal", "reina"));
		addUser(new AccountUser("Mohamad", "YASSINE", "mohamad", "yassine"));
		addUser(new AccountUser("Frédéric", "GANTOIS", "frederic", "gantois"));
		addUser(new AccountUser("Alexis", "LE ROY", "alexis", "leroy"));
		addUser(new AccountUser("Kyung Mo", "YANG", "kyungmo", "yang"));
		addUser(new AccountUser("Amar", "LANKRI", "amar", "lankri"));
		addUser(new AccountUser("Syed Farath", "SAYEED", "syedfarath", "sayeed"));
		addUser(new AccountUser("Benoît", "GRASSET", "benoit", "grasset"));
		addUser(new AccountUser("Nathan", "MARGUET", "nathan", "marguet"));
		addUser(new AccountUser("Didier", "TERRIER", "didier", "terrier"));
		addUser(new AccountUser("Antoine", "MUNCK", "antoine", "munck"));
		
		addTopic(new Topic("Administrator"));
		addTopic(new Topic("Karren"));
		addTopic(new Topic("Stagiaires"));
		
		getUser("abdoulaye").subscribeToTopic(getTopic("Administrator"));
		getUser("abdoulaye").subscribeToTopic(getTopic("Stagiaires"));
		getUser("abdoulaye").subscribeToTopic(getTopic("Karren"));
		getUser("abdoulaye").subscribeToTopic(getTopic("Administrator"));
		getUser("didier").subscribeToTopic(getTopic("Administrator"));
		getUser("antoine").subscribeToTopic(getTopic("Administrator"));
		getUser("patrick").subscribeToTopic(getTopic("Administrator"));
		getUser("maud").subscribeToTopic(getTopic("Stagiaires"));
		getUser("alexis").subscribeToTopic(getTopic("Stagiaires"));
		getUser("hamza").subscribeToTopic(getTopic("Stagiaires"));
		getUser("kyungmo").subscribeToTopic(getTopic("Stagiaires"));
		getUser("jerome").subscribeToTopic(getTopic("Karren"));
	}
	
	public static AccountUser addUser(AccountUser user) {
		return users.put(user.getLogin(), user);
	}
	public static Topic addTopic(Topic topic) {
		return topics.put(topic.getName(), topic);
	}
	public static boolean addMessage(ExchangeMessage message) {
		return uploadedMessages.add(message);
	}
	public static boolean removeUser(AccountUser user) {
		return users.remove(user.getLogin(), user);
	}
	public static boolean removeTopic(Topic topic) {
		return topics.remove(topic.getName(), topic);
	}
	public static boolean removeMessage(ExchangeMessage message) {
		return uploadedMessages.remove(message);
	}
	public static AccountUser getUser(String login) {
		return users.get(login);
	}
	public static Topic getTopic(String topic) {
		return topics.get(topic);
	}
	public static Map<String, AccountUser> getUsers() {
		return Collections.unmodifiableMap(users);
	}
	public static Map<String, Topic> getTopics() {
		return Collections.unmodifiableMap(topics);
	}
	public static Set<ExchangeMessage> getUploadedMessages() {
		return Collections.unmodifiableSet(uploadedMessages);
	}
}
