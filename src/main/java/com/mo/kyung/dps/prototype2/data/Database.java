package com.mo.kyung.dps.prototype2.data;

import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import com.mo.kyung.dps.prototype2.data.datatypes.AccountUser;
import com.mo.kyung.dps.prototype2.data.datatypes.EEnnuum;
import com.mo.kyung.dps.prototype2.data.datatypes.ExchangeMessage;
import com.mo.kyung.dps.prototype2.data.datatypes.Topic;
import com.mo.kyung.dps.prototype2.websocket.Constants;

public class Database {
	private static AccountUser admin = new AccountUser("admin", "admin", "admin", "admin");
	private static Set<AccountUser> users = new TreeSet<AccountUser>();
	private static Set<Topic> topics = new TreeSet<Topic>();
	private static Map<String, AccountUser> connectedUsers = new TreeMap<String, AccountUser>(); // String is for the
																					// token
	private static Set<ExchangeMessage> uploadedMessages = new TreeSet<ExchangeMessage>();
	static {
		System.out.println("bonjour");
		System.out.println(EEnnuum.ONE);
		addTopic(new Topic(Constants.getAdministrationTopic(), true));
		addTopic(new Topic(Constants.getConnectionTopic(), true));
		addTopic(new Topic(Constants.getNewTopicTopic(), true));
		addTopic(new Topic("test", true));
		addTopic(new Topic("special", false));

		addUser(admin);

		addUser(new AccountUser("Patrick", "GRIMBERG", "patrick", "grimberg"));
		addUser(new AccountUser("Jerome", "DAZIANO", "jerome", "daziano"));
		addUser(new AccountUser("Maud", "CADORET", "maud", "cadoret"));
		addUser(new AccountUser("Hamza", "OUCHEIKH", "hamza", "oucheikh"));
		addUser(new AccountUser("Abdoulaye", "SOW", "abdoulaye", "sow"));
		addUser(new AccountUser("Pascal", "REINA", "pascal", "reina"));
		addUser(new AccountUser("Mohamad", "YASSINE", "mohamad", "yassine"));
		addUser(new AccountUser("Frederic", "GANTOIS", "frederic", "gantois"));
		addUser(new AccountUser("Alexis", "LE ROY", "alexis", "leroy"));
		addUser(new AccountUser("Kyung Mo", "YANG", "kyungmo", "yang"));
		addUser(new AccountUser("Amar", "LANKRI", "amar", "lankri"));
		addUser(new AccountUser("Syed Farath", "SAYEED", "syedfarath", "sayeed"));
		addUser(new AccountUser("Benoit", "GRASSET", "benoit", "grasset"));
		addUser(new AccountUser("Nathan", "MARGUET", "nathan", "marguet"));
		addUser(new AccountUser("Didier", "TERRIER", "didier", "terrier"));
		addUser(new AccountUser("Antoine", "MUNCK", "antoine", "munck"));
		addUser(new AccountUser("Hugo", "MASSELOT", "hugo", "masselot"));
		addUser(new AccountUser("Léo", "REMY", "leo", "remy"));
		addUser(new AccountUser("Henri", "BAUER", "henri", "bauer"));
		addUser(new AccountUser("Ladjel", "BELLATRECHE", "ladjel", "bellatreche"));

		uploadMessaage(new ExchangeMessage(getUser("admin"), getTopic("test"), "Welcome", new Date()));
		getUser("admin").subscribeToTopic(getTopic("special"));
		getUser("kyungmo").subscribeToTopic(getTopic("special"));
	}

	public static Set<AccountUser> getUsers() {
		return Collections.unmodifiableSet(users);
	}

	public static AccountUser getUser(String login) {
		for (AccountUser user : users) {
			if (user.getLogin().equals(login)) {
				return user;
			}
		}
		return null;// user does not exist
	}

	public static boolean addUser(AccountUser user) {
		if (users.add(user)) {
			for (Topic topic : topics) {
				if (topic.isPublic()) {
					user.subscribeToTopic(topic);
				}
			}
			return true;
		}
		return false;
	}

	public static boolean removeUser(AccountUser user) {
		return users.remove(user);
	}

	public static Set<Topic> getTopics() {
		return Collections.unmodifiableSet(topics);
	}

	public static Topic getTopic(String topicName) {
		for (Topic topic : topics) {
			if (topic.getName().equals(topicName)) {
				return topic;
			}
		}
		return null; // topic does not exist
	}

	public static boolean addTopic(Topic topic) {
		return topics.add(topic);
	}

	public static boolean removeTopic(Topic topic) {
		return topics.remove(topic);
	}

	public static Map<String, AccountUser> getConnectedUsers() {
		return Collections.unmodifiableMap(connectedUsers);
	}

	public static AccountUser getConnectedUser(String login) {
		for (AccountUser user : connectedUsers.values()) {
			if (user.getLogin().equals(login)) {
				return user;
			}
		}
		return null;// user does not exist or is not connected
	}

	public static boolean addConnectedUser(AccountUser user) throws UnsupportedEncodingException {
		if (!connectedUsers.containsValue(user)) {
			user.logIn();
			connectedUsers.put(user.getToken(), user);
			return true;
		}
		return false;
	}

	public static boolean removeConnectedUser(AccountUser user) {
		String token = user.getToken();
		user.logOut();
		return connectedUsers.remove(token, user);
	}

	public static Set<ExchangeMessage> getUploadedMessages() {
		return Collections.unmodifiableSet(uploadedMessages);
	}

	public static boolean uploadMessaage(ExchangeMessage message) {
		return uploadedMessages.add(message);
	}

	public static boolean deleteMessage(ExchangeMessage message) {
		return uploadedMessages.remove(message);
	}

	public static boolean applySubscription(String login, String topicName) {
		return getUser(login).subscribeToTopic(getTopic(topicName));
	}

	public static Set<String> getConnectedUsersLogin() {
		Set<String> connectesUsersLogin = new TreeSet<String>();
		for (AccountUser user : connectedUsers.values()) {
			connectesUsersLogin.add(user.getLogin());
		}
		return connectesUsersLogin;
	}

	public static Set<String> getConnectedUsersAsString() {
		Set<String> logins = new HashSet<>();
		for (AccountUser user : connectedUsers.values()) {
			logins.add(user.getLogin());
		}
		return logins;
	}
}
