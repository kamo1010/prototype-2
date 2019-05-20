package com.mo.kyung.dps.prototype2.websocket;

import java.util.Date;
import java.util.Objects;

import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.datatypes.Topic;
import com.mo.kyung.dps.prototype2.data.representations.ReceivedMessageRepresentation;
import com.mo.kyung.dps.prototype2.data.representations.SentMessageRepresentation;
import com.mo.kyung.dps.prototype2.data.representations.UserPropertiesRepresentation;

@ServerEndpoint(value = "/{login}", encoders = NotificationEncoder.class, decoders = NotificationDecoder.class)
public class NotificationEndpoint {
	@OnOpen
	public void onOpen(@PathParam("login") final String login, final Session session)
			throws RegistrationFailedException, JsonProcessingException {
		if (Objects.isNull(login) || login.isEmpty()) {
			throw new RegistrationFailedException("User name is required");
		} else {
			session.getUserProperties().put(Constants.getUserNameKey(), login);
			if (NotificationSessionManager.register(session)) {
				System.out.printf("Session opened for %s\n", login);
				NotificationSessionManager.publish(
						new ReceivedMessageRepresentation(
								login,
								Constants.getConnectionTopic(),
								Constants.getMapper().writeValueAsString(Database.getConnectedUsersAsString()),
								new Date()),
						session);
				NotificationSessionManager.publishOnce(
						new ReceivedMessageRepresentation(
								login,
								Constants.getAdministrationTopic(),
								Constants.getMapper().writeValueAsString(new UserPropertiesRepresentation(Database.getConnectedUser(login))),
								new Date()),
						session);
				System.out.println(Database.getConnectedUser(login).getTopics().isEmpty());
			} else {
				throw new RegistrationFailedException("Unable to register, username already exists, try another");
			}
		}
	}
//AccountUser accountUser, Topic topic, String payload, Date date
	@OnError
	public void onError(final Session session, final Throwable throwable) {
		if (throwable instanceof RegistrationFailedException) {
			NotificationSessionManager.close(session, CloseCodes.VIOLATED_POLICY, throwable.getMessage());
		}
	}

	@OnMessage
	public void onMessage(final SentMessageRepresentation message, final Session session) {
		System.out.println("New message incoming..");
	}

	@OnClose
	public void onClose(final Session session) throws JsonProcessingException {
		if (NotificationSessionManager.remove(session)) {
			String login = (String) session.getUserProperties().get(Constants.getUserNameKey());
			System.out.printf("Session closed for %s\n", session.getUserProperties().get(Constants.getUserNameKey()));
			Database.removeConnectedUser(Database.getConnectedUser(login));
			NotificationSessionManager.publish(
					new ReceivedMessageRepresentation(
							login,
							Constants.getConnectionTopic(),
							Constants.getMapper().writeValueAsString(Database.getConnectedUsersLogin()),
							new Date()),
					session);
		}
	}
}
