package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class ScopeParserTest {

	protected ScopeParser parser = new ScopeParser(); 
	
	@Test
	public void testParsing()
	{
		assertEquals(null, parser.parseScope(null));
		assertTrue(parser.parseScope("").isEmpty());
		assertTrue(parser.parseScope("scope").contains("scope"));
		assertTrue(parser.parseScope("sco/pe").contains("sco/pe"));
		assertTrue(parser.parseScope("scope scope1").contains("scope1"));
		assertTrue(parser.parseScope("scope scope1  ").contains("scope1"));
		assertTrue(parser.parseScope("  scope scope1").contains("scope1"));
		assertTrue(parser.parseScope("  scope Scope1").contains("Scope1"));
		assertTrue(parser.parseScope("  scope Scope1!").contains("Scope1!"));
		
		parser.parseScope("  scope sc/ope1");
	}
	
	@Test
	public void testRender()
	{
		Set<String> scopes = new HashSet<>(Arrays.asList("aa","bb","cc"));
		
		assertNull(parser.render(null));
		assertEquals("aa bb cc", parser.render(scopes));
		
		assertEquals(scopes, parser.parseScope(parser.render(scopes)));
		
		scopes = new HashSet<>(Arrays.asList("aa"));
		assertEquals("aa", parser.render(scopes));
		
		scopes = new HashSet<>();
		assertEquals("", parser.render(scopes));
		
	}
	
}
