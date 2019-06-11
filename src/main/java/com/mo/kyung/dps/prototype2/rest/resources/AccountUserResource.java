package com.mo.kyung.dps.prototype2.rest.resources;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.datatypes.AccountUser;
import com.mo.kyung.dps.prototype2.data.datatypes.ExchangeMessage;
import com.mo.kyung.dps.prototype2.data.datatypes.Topic;
import com.mo.kyung.dps.prototype2.data.representations.ReceivedMessageRepresentation;
import com.mo.kyung.dps.prototype2.data.representations.SentMessageRepresentation;
import com.mo.kyung.dps.prototype2.data.representations.UserPropertiesRepresentation;
import com.mo.kyung.dps.prototype2.websocket.Constants;
import com.mo.kyung.dps.prototype2.websocket.NotificationSessionManager;

@Path("{login}")
public class AccountUserResource {
	@GET
	@Produces(MediaType.APPLICATION_JSON) // method used to fill the properties tab
	public Response getPersonalDetails(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token) throws UnsupportedEncodingException, JsonProcessingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("@101@")[0])) {
				return Response.ok(Constants.getMapper()
						.writeValueAsString(new UserPropertiesRepresentation(Database.getUser(login)))).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}

	@POST
	@Path("post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAMessage(@PathParam(value = "login") String login, SentMessageRepresentation message,
			@HeaderParam(value = "token") String token) throws URISyntaxException, UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("@101@")[0])) {
				if (Database.getTopic(message.getTopic()) != null) {
					if (Database.getConnectedUser(login).getTopics().contains(Database.getTopic(message.getTopic()))) {
						ExchangeMessage msg = new ExchangeMessage(Database.getUser(login),
						Database.getTopic(message.getTopic()), message.getPayload());
						Database.uploadMessaage(msg);
						/* for (AccountUser user : Database.getConnectedUsers().values()) {
							if (user.isInterestedIn(Database.getTopic(message.getTopic()))) {
								NotificationSessionManager.publishToOne(
										new ReceivedMessageRepresentation(login, message.getTopic(),
												message.getPayload(), new Date()),
										NotificationSessionManager.getSession(user.getLogin()));
							}
						} */
						NotificationSessionManager.publishToAll(new ReceivedMessageRepresentation(msg.getUser().getLogin(), msg.getTopic().getName(),
						msg.getPayload(), msg.getEditionDate()),
				NotificationSessionManager.getASession());
						return Response.created(new URI(new StringBuilder(login).append("/notifications").toString())).build();
					} else {
						return Response.status(Status.FORBIDDEN).build();
					}
				} else {
					return Response.status(Status.NOT_ACCEPTABLE).build();
				}
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}

	@GET
	@Path("notifications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotifications(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token) throws UnsupportedEncodingException, JsonProcessingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("@101@")[0])) {
				AccountUser user = Database.getConnectedUser(login);
				List<ReceivedMessageRepresentation> messages = new ArrayList<ReceivedMessageRepresentation>();
				if (!Database.getUploadedMessages().isEmpty()) {
					for (ExchangeMessage message : Database.getUploadedMessages()) {
						if (user.isInterestedIn(message.getTopic())) {
							ReceivedMessageRepresentation receivedMessage = new ReceivedMessageRepresentation(
									message.getUser().getLogin(), message.getTopic().getName(), message.getPayload(),
									message.getEditionDate());
							messages.add(receivedMessage);
						}

					}
				}
				return Response.ok(Constants.getMapper().writeValueAsString(messages)).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}

	@POST
	@Path("topics")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response createNewTopic(@PathParam(value = "login") String login, @HeaderParam(value = "token") String token,
			String newTopic)
			throws URISyntaxException, UnsupportedEncodingException, JsonProcessingException, ParseException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("@101@")[0])) {
				Topic topic = new Topic(newTopic, false);
				if (!topic.equals(Database.getTopic(newTopic))) {
					AccountUser user = Database.getConnectedUser(login);
					Database.addTopic(topic);
					user.subscribeToTopic(topic);
					ReceivedMessageRepresentation receivedMessage = new ReceivedMessageRepresentation(login,
							Constants.getNewTopicTopic(), login + " has created a new topic : " + topic.getName(),
							new Date());
					Database.uploadMessaage(receivedMessage.toExchangeMessage());
					NotificationSessionManager.publishToAll(receivedMessage,
							NotificationSessionManager.getSession(login));
					NotificationSessionManager.publishToOne(new ReceivedMessageRepresentation(login,
							Constants.getAdministrationTopic(),
							Constants.getMapper().writeValueAsString(
									new UserPropertiesRepresentation(user)),
							new Date()), NotificationSessionManager.getSession(login));
					return Response.created(new URI(new StringBuilder(login).append("/topics/").toString())).build();
				}
				return Response.status(418).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}

	@PUT
	@Path("topics/{topic_name}")
	public Response unsubscribeFromTopic(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token, @PathParam(value = "topic_name") String topicName)
			throws URISyntaxException, UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("@101@")[0])) {
				AccountUser user = Database.getUser(login);
				Topic topic = Database.getTopic(topicName);
				if (user.getTopics().contains(topic)) {
					user.unsubscribe(topic);
					return Response.ok().build();
				}
				return Response.status(418, "You cannot unsubscribe from a topic you haven't subscribed to.").build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}

	@PUT
	@Path("subscribe")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response subscribe(@PathParam(value = "login") String login, @HeaderParam(value = "token") String token,
			String topicName)
			throws UnsupportedEncodingException, URISyntaxException, ParseException, JsonProcessingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("@101@")[0])) {
				if (Database.getTopic(topicName) != null) {
					AccountUser user = Database.getUser(login);
					Topic topic = Database.getTopic(topicName);
					if (!user.getTopics().contains(topic)) {
						if (user.subscribeToTopic(topic)) {
							ReceivedMessageRepresentation receivedMessage = new ReceivedMessageRepresentation(login,
									topicName, login + " has subscribed to the topic : " + topic.getName(), new Date());
							Database.uploadMessaage(receivedMessage.toExchangeMessage());
							NotificationSessionManager.publishToAll(receivedMessage,
									NotificationSessionManager.getSession(login));
							NotificationSessionManager
									.publishToOne(
											new ReceivedMessageRepresentation(login, Constants.getAdministrationTopic(),
													Constants.getMapper()
															.writeValueAsString(new UserPropertiesRepresentation(
																	Database.getConnectedUser(login))),
													new Date()),
											NotificationSessionManager.getSession(login));
							return Response.ok().build();
						} else {
							return Response.status(Status.BAD_REQUEST).build();
						}
					} else {
						return Response.status(418, "You cannot invite users to a topic you haven't subscribed to")
								.build();
					}
				} else {
					return Response.status(Status.CONFLICT).build();
				}
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
}
