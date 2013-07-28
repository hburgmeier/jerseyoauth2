package com.github.hburgmeier.jerseyoauth2.rs.api;

import java.util.EnumSet;

import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.api.types.TokenType;

public interface IRSConfiguration {

	EnumSet<ParameterStyle> getSupportedOAuthParameterStyles();

	EnumSet<TokenType> getSupportedTokenTypes();

}
