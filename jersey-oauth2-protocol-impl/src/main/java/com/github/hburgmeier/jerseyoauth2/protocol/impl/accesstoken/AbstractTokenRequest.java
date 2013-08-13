package com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;

public abstract class AbstractTokenRequest implements IAccessTokenRequest {

	public abstract void validate() throws OAuth2ParseException;
}
