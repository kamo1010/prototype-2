package com.mo.kyung.dps.prototype2.rest.services;

import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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

import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.datatypes.AccountUser;
import com.mo.kyung.dps.prototype2.data.datatypes.ExchangeMessage;
import com.mo.kyung.dps.prototype2.data.datatypes.Topic;
import com.mo.kyung.dps.prototype2.data.resources.ExchangeMessageResource;
import com.mo.kyung.dps.prototype2.data.resources.InvitationResource;

@Path("{login}")
public class AccountUserService {
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public Response getPersonalDetails(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token) throws UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("+101+")[0])) {
				return Response.ok(Database.getUser(login).buildUserDetailResource()).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
	@POST
	@Path("post")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response postAMessage(@PathParam(value = "login") String login, ExchangeMessageResource message,
			@HeaderParam(value = "token") String token) throws URISyntaxException, UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("+101+")[0])) {
				Database.addMessage(new ExchangeMessage(login, message, new Date()));
				StringBuilder builder = new StringBuilder(login).append("notifications");
				return Response.created(new URI(builder.toString())).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
	@GET
	@Path("notifications")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getNotifications(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token) throws UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("+101+")[0])) {
				AccountUser user = Database.getUser(login);
				List<ExchangeMessageResource> messages = new ArrayList<ExchangeMessageResource>();
				for (ExchangeMessage exchangeMessage : Database.getUploadedMessages()) {
					if (user.isInterestedIn(exchangeMessage.getTopic())) {
						messages.add(exchangeMessage.buildResource());
					}
				}
				return Response.ok(messages).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
	@GET
	@Path("topics")
	@Produces(MediaType.APPLICATION_JSON)
	public Response getInterestTopics(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token) throws UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString()).split("+101+")[0])) {
				return Response.ok(Database.getUser(login).getTopics()).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
	@POST
	@Path("topics")
	public Response createNewTopic(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token,
			@HeaderParam(value = "topic_name") String topicName) throws URISyntaxException, UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("+101+")[0])) {
				if (Database.getTopics().containsKey(topicName)) {
					Topic topic = new Topic(topicName);
					AccountUser user = Database.getUser(login);
					Database.addTopic(topic);
					user.subscribeToTopic(topic);
				}
				StringBuilder builder = new StringBuilder(login).append("topics/").append(topicName);
				return Response.created(new URI(builder.toString())).build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
	@POST
	@Path("topics/{topic_name}")
	public Response unsubscribeFromTopic(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token,
			@PathParam(value = "topic_name") String topicName) throws URISyntaxException, UnsupportedEncodingException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("+101+")[0])) {
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
	@Path("invite")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response inviteUser(@PathParam(value = "login") String login,
			@HeaderParam(value = "token") String token,
			InvitationResource invitation) throws UnsupportedEncodingException, URISyntaxException {
		if (token.isEmpty()) {
			return Response.status(Status.UNAUTHORIZED).build();
		} else {
			if (login.equals(new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString())
					.split("+101+")[0])) {
				AccountUser user = Database.getUser(login);
				Topic topic = Database.getTopic(invitation.getTopicName());
				if (user.getTopics().contains(topic)) {
					for (String inviteeLogin : invitation.getLogins()) {
						Database.getUser(inviteeLogin).subscribeToTopic(topic);
					}
				}
				return Response.status(418, "You cannot invite users to a topic you haven't subscribed to").build();
			} else {
				return Response.status(Status.FORBIDDEN).build();
			}
		}
	}
}
