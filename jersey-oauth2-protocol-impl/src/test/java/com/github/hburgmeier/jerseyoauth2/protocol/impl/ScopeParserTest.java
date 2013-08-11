package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class ScopeParserTest {

	@Test
	public void testParsing()
	{
		ScopeParser parser = new ScopeParser();
		
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
	
}
