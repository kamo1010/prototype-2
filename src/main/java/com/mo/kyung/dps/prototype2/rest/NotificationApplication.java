package com.mo.kyung.dps.prototype2.rest;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

import com.mo.kyung.dps.prototype2.rest.resources.AccountUserResource;
import com.mo.kyung.dps.prototype2.rest.resources.AuthenticationResource;

public class NotificationApplication extends Application {
	private Set<Class<?>> classes = new HashSet<Class<?>>();

	public NotificationApplication() {
		classes.addAll(Arrays.asList(AuthenticationResource.class, AccountUserResource.class));
	}

	@Override
	public Set<Class<?>> getClasses() {
		return classes;
	}
}
