package com.burgmeier.jerseyoauth2.authsrv.api.token;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;

public interface ITokenService {
	
	void issueToken(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException;

	void sendTokenResponse(HttpServletResponse response, IAccessTokenInfo accessTokenInfo) throws OAuthSystemException,
			IOException;
	
	void sendErrorResponse(HttpServletResponse response, OAuthProblemException ex) throws OAuthSystemException,
	IOException;

	
}
