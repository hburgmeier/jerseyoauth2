package com.github.hburgmeier.jerseyoauth2.protocol.impl;

import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;

public class ScopeParser {

	private static final Pattern SCOPE_PATTERN = Pattern.compile("[!#$%&'\\(\\)*+,\\-\\.\\/0-9:;<=>?@A-Z\\[\\]^_`a-z{|}~]+");
	
	public Set<String> parseScope(String scope)
	{
		if (scope==null)
			return null;
		
		Scanner scanner = new Scanner(StringUtils.trim(scope));
		try {
			scanner.useDelimiter(" ");
			
			Set<String> result = new HashSet<>();
			while (scanner.hasNext(SCOPE_PATTERN))
			{
				String scopeToken = scanner.next(SCOPE_PATTERN);
				result.add(scopeToken);
			}
			return result;
		} finally {
			scanner.close();
		}
	}
}
