package com.burgmeier.jerseyoauth2.authsrv.api.token;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.types.ResponseType;

public interface ITokenService {
	
	void handleRequest(HttpServletRequest request, HttpServletResponse response, OAuthTokenRequest oauthRequest)
			throws OAuthSystemException, IOException, OAuthProblemException;
	
	void issueNewToken(HttpServletRequest request, HttpServletResponse response, IAuthorizedClientApp clientApp, ResponseType responseType)
			throws OAuthProblemException, OAuthSystemException, IOException;
	
	void sendTokenResponse(HttpServletRequest request, HttpServletResponse response, IAccessTokenInfo accessTokenInfo, ResponseType responseType) throws OAuthSystemException,
			IOException;
	
	void sendErrorResponse(HttpServletResponse response, OAuthProblemException ex) throws OAuthSystemException,
			IOException;

	
}
