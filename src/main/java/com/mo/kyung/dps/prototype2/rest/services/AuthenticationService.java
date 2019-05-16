package com.mo.kyung.dps.prototype2.rest.services;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.mo.kyung.dps.prototype2.data.Database;
import com.mo.kyung.dps.prototype2.data.datatypes.AccountUser;
import com.mo.kyung.dps.prototype2.data.representations.AccountUserRepresentation;


//this service is ok
@Path("auth")
public class AuthenticationService {
	@POST
	@Path("in")
	@Consumes(MediaType.APPLICATION_JSON)
	public Response logIn(AccountUserRepresentation credentials) throws UnsupportedEncodingException {
		for (AccountUser user : Database.getUsers().values()) {
			if (user.getLogin().equals(credentials.getLogin()) && user.getPassword().equals(credentials.getPassword())) {
				if (!user.isConnected()) {
					StringBuilder builder = new StringBuilder(credentials.getLogin()).append("@101@").append(credentials.getPassword());
					user.setToken(Base64.getEncoder().encodeToString(builder.toString().getBytes(StandardCharsets.UTF_8.toString())));
					return Response.ok(user.getToken()).build();
				} else {
					return Response.status(418).build();
				}
			}
		}
		return Response.status(400, "Login not found.").build();
	}
	@POST
	@Path("out")
	public Response logOut(@HeaderParam(value = "token") String token) throws UnsupportedEncodingException {
		String login = new String(Base64.getDecoder().decode(token), StandardCharsets.UTF_8.toString()).split("@101@")[0];
		if (Database.getUser(login).isConnected()) {
			Database.getUser(login).setToken("");;
			return Response.ok().build();
		}
		return Response.status(418).build();
	}
}