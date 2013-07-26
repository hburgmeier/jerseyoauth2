package com.github.hburgmeier.jerseyoauth2.rs.api;

import org.apache.amber.oauth2.common.message.types.ParameterStyle;

public interface IRSConfiguration {

	ParameterStyle[] getSupportedOAuthParameterStyles();

}
