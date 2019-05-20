package com.mo.kyung.dps.prototype2.data.representations;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.datatypes.ExchangeMessage;

/*
 * representation of messages when received by users
 */
public class ReceivedMessageRepresentation {
	private String login;
	private String topic;
	private String payload;
	private String editionDate;

	public ReceivedMessageRepresentation() {
		super();
	}

	public ReceivedMessageRepresentation(String login, String topic, String payload, Date date) {
		super();
		this.login = login;
		this.topic = topic;
		this.payload = payload;
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy   -   HH:mm:ss");
		this.editionDate = simpleDateFormat.format(date);
	}

	public String getAuthor() {
		return login;
	}

	public void setAuthor(String login) {
		this.login = login;
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

	public ExchangeMessage toExchangeMessage() throws ParseException {
		return new ExchangeMessage(Database.getUser(login), Database.getTopic(topic), payload, new SimpleDateFormat("dd-MM-yyyy   -   HH:mm:ss").parse(editionDate));
	}
}
