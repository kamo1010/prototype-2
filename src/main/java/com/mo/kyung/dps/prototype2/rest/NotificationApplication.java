package com.mo.kyung.dps.prototype2.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.mo.kyung.dps.prototype2.rest.services.AccountUserService;
import com.mo.kyung.dps.prototype2.rest.services.AuthenticationService;

public class NotificationApplication extends Application {
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public NotificationApplication() {
		classes.addAll(Arrays.asList(AuthenticationService.class, AccountUserService.class));
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
}
