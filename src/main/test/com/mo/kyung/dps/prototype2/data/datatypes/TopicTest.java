package com.mo.kyung.dps.prototype2.data.datatypes;

import java.util.TreeSet;

import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.mockito.Mock;
import org.mockito.Mockito;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class TopicTest {

	Topic t1 = new Topic();
	Topic t2 = new Topic("topic", true);
	Topic t3 = new Topic("topik", true);
	@Mock
	AccountUser ua = new AccountUser("Alexis", "Le Roy", "alexis", "leroy");
	@Mock
	AccountUser ua2 = new AccountUser();
	
	@BeforeClass
	public static void initialize() {
		System.out.println("Before Topic Test Class\n");
	}
	
	@Test
	public void t01_getters_and_setters() {
		Assert.assertEquals(t2.getName(), "topic");
		Assert.assertEquals(t2.getSubscribers(), new TreeSet<AccountUser>());
		Assert.assertEquals(t2.isPublic(), true);
		System.out.println("getters and setters tested");
	}
	
	@Test
	public void t02_comparison() {
		Mockito.when(ua.getClass()).thenReturn(null);
		Assert.assertEquals(t2.equals(t1), false);
		Assert.assertEquals(t2.equals(t2), true);
		Assert.assertEquals(t1.equals(new Topic()), true);
		Assert.assertEquals(t1.equals(null), false);
		System.out.println("comparison tested");
	}
	
	@AfterClass
	public static void terminate() {
		System.out.println("\nAfter Topic Test Class");
	}
}
