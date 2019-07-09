package com.mo.kyung.dps.prototype2.data.datatypes;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashSet;
import java.util.Set;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class AccountUserTest {
	
	AccountUser ua = new AccountUser("Alexis", "Le Roy", "alexis", "leroy");
	AccountUser ua2 = new AccountUser();
	@Mock
	Topic t;
	@Mock
	HashSet<Topic> set;
	
	@Rule public MockitoRule mockitoRule = MockitoJUnit.rule();
	
	@BeforeClass
	public static void initialize() {
		System.out.println("Before AccountUser Test Class\n");
	}
	
	@Test
	public void t01_getters_setters() {
		Assert.assertEquals(ua.getFirstName(), "Alexis");
		Assert.assertEquals(ua.getLastName(), "Le Roy");
		Assert.assertEquals(ua.getLogin(), "alexis");
		Assert.assertEquals(ua.getPassword(), "leroy");
		Assert.assertEquals(ua.getToken(), "");
		
		ua.setToken("aaa");
		ua.changeLogin("Logan");
		ua.changePassword("yang");
		Assert.assertEquals(ua.getToken(), "aaa");
		Assert.assertEquals(ua.getLogin(), "Logan");
		Assert.assertEquals(ua.getPassword(), "yang");
		System.out.println("getters and setters tested");
	}
	
	@Test
	public void t02_compareToEmptyConstructor() {
		ua2.changeLogin("alexis");
		ua2.changePassword("leroy");
		Assert.assertEquals(ua.compareTo(ua2), 0);
		System.out.println("compareToEmptyConstructor tested");
	}

	@Test
	public void t03_connect() throws UnsupportedEncodingException {
		ua.logIn();
		Assert.assertTrue(ua.isConnected());
		Assert.assertEquals(ua.getToken(),
				Base64.getEncoder().encodeToString(new StringBuilder(ua.getLogin()).append("@101@").append(ua.getPassword()).toString().getBytes(StandardCharsets.UTF_8.toString())));
		ua.logIn();
		Assert.assertEquals(ua.getToken(),
				Base64.getEncoder().encodeToString(new StringBuilder(ua.getLogin()).append("@101@").append(ua.getPassword()).toString().getBytes(StandardCharsets.UTF_8.toString())));
		ua.logOut();
		Assert.assertFalse(ua.isConnected());
		ua.logOut();
		Assert.assertFalse(ua.isConnected());
		System.out.println("connect tested");
	}
	
	@Test
	public void t04_subscription() {
		Set<Topic> tt = ua.getTopics();
		Mockito.when(t.addSubscriber(ua)).thenReturn(true);
		Mockito.when(t.removeSubscriber(ua)).thenReturn(true);
		Assert.assertEquals(tt, new HashSet<>());
		Assert.assertEquals(ua.isInterestedIn(t), false);
		Assert.assertEquals(ua.subscribeToTopic(t), true);
		Assert.assertEquals(ua.subscribeToTopic(t), false);
		Assert.assertEquals(ua.isInterestedIn(t), true);
		Assert.assertEquals(ua.unsubscribe(t), true);
		Assert.assertEquals(ua.unsubscribe(t), false);
		Assert.assertEquals(ua.isInterestedIn(t), false);
	
		Mockito.when(t.addSubscriber(ua)).thenReturn(false);
		Mockito.when(t.removeSubscriber(ua)).thenReturn(false);
		Assert.assertEquals(ua.subscribeToTopic(t), false);
		Assert.assertEquals(ua.unsubscribe(t), false);
		System.out.println("subscription tested");
	}
	
	@AfterClass
	public static void terminate() {
		System.out.println("\nAfter AccountUser Test Class");
	}
}
