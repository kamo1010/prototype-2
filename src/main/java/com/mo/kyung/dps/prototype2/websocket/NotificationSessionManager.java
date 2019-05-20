package com.mo.kyung.dps.prototype2.websocket;

import java.io.IOException;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import javax.websocket.CloseReason;
import javax.websocket.CloseReason.CloseCodes;
import javax.websocket.EncodeException;
import javax.websocket.Session;

import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.representations.ReceivedMessageRepresentation;

public class NotificationSessionManager {
	private static final Lock LOCK = new ReentrantLock();
    private static final Set<Session> SESSIONS = new CopyOnWriteArraySet<>();
    private static final int openSessionLimitNumber = 15;

    private NotificationSessionManager() {
        throw new IllegalStateException(Constants.getInstantiationNotAllowed());
    }

    public static Set<Session> getSessions() {
		return Collections.unmodifiableSet(SESSIONS);
	}

	public static void publish(final ReceivedMessageRepresentation receivedMessageRepresentation, final Session origin) {
        assert !Objects.isNull(receivedMessageRepresentation) && !Objects.isNull(origin);
        SESSIONS.stream().forEach(session -> {
            try {//gotta check who is concerned
                if (Database.getConnectedUser((String) session.getUserProperties().get(Constants.getUserNameKey())).isInterestedIn(Database.getTopic(receivedMessageRepresentation.getTopic()))) {
                	session.getBasicRemote().sendObject(receivedMessageRepresentation);
                }
            } catch (IOException | EncodeException e) {
                e.printStackTrace();
            }
        });
    }

    static boolean register(final Session session) {
        assert !Objects.isNull(session);

        boolean result = false;
        try {
            LOCK.lock();

            result = !SESSIONS.contains(session) && SESSIONS.size() < openSessionLimitNumber && !SESSIONS.stream()
                    .filter(elem -> ((String) elem.getUserProperties().get(Constants.getUserNameKey())).equals((String) session.getUserProperties().get(Constants.getUserNameKey())))
                    .findFirst().isPresent() && SESSIONS.add(session);
        } finally {
            LOCK.unlock();
        }
        return result;
    }
    
    static void close(final Session session, final CloseCodes closeCode, final String message) {
        assert !Objects.isNull(session) && !Objects.isNull(closeCode);
        
        try {
            session.close(new CloseReason(closeCode, message));
        } catch (IOException e) {
            throw new RuntimeException("Unable to close session", e);
        }
    }

    static boolean remove(final Session session) {
        assert !Objects.isNull(session);

        return SESSIONS.remove(session);
    }
}
