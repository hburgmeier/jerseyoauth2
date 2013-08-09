package com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;

public class FormExtractorTest {

	@Test
	public void testFormExtraction()
	{
		IHttpRequest request = mock(IHttpRequest.class);
		when(request.getFormParameterValue(eq("testField"))).thenReturn("test");
		
		FormExtractor extractor = new FormExtractor("testField");
		assertEquals("test", extractor.extractValue(request));
		
		when(request.getFormParameterValue(eq("testField"))).thenReturn(null);
		assertEquals(null, extractor.extractValue(request));
		
		when(request.getFormParameterValue(eq("testField"))).thenReturn("");
		assertEquals(null, extractor.extractValue(request));
	}
	
}
