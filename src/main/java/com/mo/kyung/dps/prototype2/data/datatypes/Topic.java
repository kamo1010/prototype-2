package com.mo.kyung.dps.prototype2.data.datatypes;

import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Topic implements Comparable<Topic> {
	private String name;
	private boolean publik;
	private Set<AccountUser> subscribers;
	public Topic() {
		super();
	}
	public Topic(String name, boolean publik) {
		super();
		this.name = name;
		this.publik = publik;
		this.subscribers = new TreeSet<AccountUser>();
	}
	public final String getName() {
		return name;
	}
	public boolean isPublic() {
		return publik;
	}
	public boolean addSubscriber(AccountUser subscriber) {
		return this.subscribers.add(subscriber);
	}
	public boolean removeSubscriber(AccountUser subscriber) {
		return this.subscribers.remove(subscriber);
	}
	public Set<AccountUser> getSubscribers() {
		return Collections.unmodifiableSet(subscribers);
	}
	@Override
	public int compareTo(Topic anotherTopic) {
		return getName().compareTo(anotherTopic.getName());//alphabetical order
	}
}
