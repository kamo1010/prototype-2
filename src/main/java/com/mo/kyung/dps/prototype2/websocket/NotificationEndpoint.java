package com.mo.kyung.dps.prototype2.websocket;

import java.util.Objects;

import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.datatypes.AccountUser;
import com.mo.kyung.dps.prototype2.data.resources.ExchangeMessageResource;

@ServerEndpoint(value = "/{login}",
				encoders = NotificationEncoder.class,
				decoders = NotificationDecoder.class)
public class NotificationEndpoint {
	@OnOpen
    public void onOpen(@PathParam("login") final String login, final Session session) throws RegistrationFailedException {
        if (Objects.isNull(login) || login.isEmpty()) {
            throw new RegistrationFailedException("User name is required");
        } else {
            session.getUserProperties().put(Constants.getUserNameKey(), login);
            if (NotificationSessionManager.register(session)) {
                System.out.printf("Session opened for %s\n", login);
                AccountUser user = Database.getUser(login);
                NotificationSessionManager.publish(
                		new ExchangeMessageResource(
                				"Administrator",
                				new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).append("***connected to the server***").toString()),
                		session);
            } else {
                throw new RegistrationFailedException("Unable to register, username already exists, try another");
            }
        }
    }

    @OnError
    public void onError(final Session session, final Throwable throwable) {
        if (throwable instanceof RegistrationFailedException) {
            NotificationSessionManager.close(session, CloseCodes.VIOLATED_POLICY, throwable.getMessage());
        }
    }

    @OnMessage
    public void onMessage(final ExchangeMessageResource message, final Session session) {
        NotificationSessionManager.publish(message, session);
    }

    @OnClose
    public void onClose(final Session session) {
        if (NotificationSessionManager.remove(session)) {
        	AccountUser user = Database.getUser(Constants.getUserNameKey());
            System.out.printf("Session closed for %s\n", session.getUserProperties().get(Constants.getUserNameKey()));
            NotificationSessionManager.publish(
            		new ExchangeMessageResource(
            				"Administrator",
            				new StringBuilder(user.getFirstName()).append(" ").append(user.getLastName()).append("***logged out of the server***").toString()),
            		session);
        }
    }
}
