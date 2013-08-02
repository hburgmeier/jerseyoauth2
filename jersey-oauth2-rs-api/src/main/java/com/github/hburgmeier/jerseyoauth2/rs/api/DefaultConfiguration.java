package com.github.hburgmeier.jerseyoauth2.rs.api;

import java.util.EnumSet;

import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;

public class DefaultConfiguration implements IRSConfiguration {

	@Override
	public EnumSet<ParameterStyle> getSupportedOAuthParameterStyles() {
		return EnumSet.of(ParameterStyle.HEADER, ParameterStyle.QUERY);
	}

	@Override
	public EnumSet<TokenType> getSupportedTokenTypes() {
		return EnumSet.of(TokenType.BEARER);
	}

}