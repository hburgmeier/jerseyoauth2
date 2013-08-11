package com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import javax.ws.rs.core.Form;

import org.junit.Before;
import org.junit.Test;

public class FormParserTest {

	protected FormParser parser;
	
	@Before
	public void setUp()
	{
		this.parser = new FormParser();
	}
	
	@Test
	public void testParse() throws FormParserException
	{
		Form f = parser.parseForm(toInputStream("a=b&c=d"));
		assertTrue(f.asMap().containsKey("a"));
		assertTrue(f.asMap().containsKey("c"));
		assertEquals("b", f.asMap().getFirst("a"));
		assertEquals("d", f.asMap().getFirst("c"));
		
		f = parser.parseForm(toInputStream("a=b"));
		
		try {
			f = parser.parseForm(toInputStream("a="));
			fail();
		} catch (Exception e) {
		}
		
		try {
			f = parser.parseForm(toInputStream("a=b&c"));
			fail();
		} catch (Exception e) {
		}
	}
	
	protected InputStream toInputStream(String str)
	{
		return new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8));
	}
	
}
