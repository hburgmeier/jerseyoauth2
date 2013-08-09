package com.github.hburgmeier.jerseyoauth2.rs.impl.rs2.filter.util;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import javax.ws.rs.core.Form;

public class FormParser {

	private static final Pattern fieldPattern = Pattern.compile("[^&=]+");
	
	public Form parseForm(InputStream inputStream) throws FormParserException
	{
		Form result = new Form();
		Scanner scanner = new Scanner(inputStream, StandardCharsets.UTF_8.name());
		scanner.useDelimiter(Pattern.compile("[&=]"));
		try {
			while (scanner.hasNext(fieldPattern))
			{
				String name = urlDecode(scanner.next(fieldPattern));
				String value = urlDecode(scanner.next(fieldPattern));
				result.param(name, value);
			}
		} catch (NoSuchElementException | UnsupportedEncodingException e) {
			throw new FormParserException(e);
		} finally {
			scanner.close();
		}
		return result;
	}
	
	protected String urlDecode(String value) throws UnsupportedEncodingException
	{
		return URLDecoder.decode(value, StandardCharsets.UTF_8.name());
	}
}
