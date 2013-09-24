package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthCodeAccessTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.IRefreshTokenRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ErrorCode;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.ClientType;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;

public class ClientIdentityValidator {

	public void validateAuthorizationRequest(IAuthorizationRequest request, IRegisteredClientApp clientApp) throws OAuth2ProtocolException
	{

	}
	
	public void validateRefreshTokenRequest(IRefreshTokenRequest refreshTokenRequest, IRegisteredClientApp clientApp) throws OAuth2ProtocolException
	{
		if (clientApp.getClientType() == ClientType.CONFIDENTIAL ||
				clientApp.getClientSecret()!=null)
		{
			if (!validate(clientApp.getClientId(), clientApp.getClientSecret(), refreshTokenRequest.getClientId(), refreshTokenRequest.getClientSecret()))
			{
				throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_CLIENT, null);
			}
		}		
	}

	public void validateAccessTokenRequest(IAuthCodeAccessTokenRequest tokenRequest, IRegisteredClientApp clientApp) throws OAuth2ProtocolException
	{
		if (clientApp.getClientType() == ClientType.CONFIDENTIAL ||
				clientApp.getClientSecret()!=null)
		{
			if (!validate(clientApp.getClientId(), clientApp.getClientSecret(), tokenRequest.getClientId(), tokenRequest.getClientSecret()))
			{
				throw new OAuth2ProtocolException(OAuth2ErrorCode.INVALID_CLIENT, null);
			}
		}		
	}	
	
	protected boolean validate(String expectedId, String expectedSecret, String actualId, String actualSecret)
	{
		return expectedId.equals(actualId) && expectedSecret.equals(actualSecret);
	}
	
}
