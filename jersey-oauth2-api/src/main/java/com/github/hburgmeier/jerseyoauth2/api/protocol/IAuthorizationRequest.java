package com.github.hburgmeier.jerseyoauth2.api.protocol;

import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;

public interface IAuthorizationRequest {

	String getClientId();
	
	ResponseType getResponseType();

	Set<String> getScopes();

	String getRedirectURI();
	
	String getState();

}
