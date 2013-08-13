package com.github.hburgmeier.jerseyoauth2.authsrv.api.client;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.api.types.ResponseType;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;


public interface IAuthorizationService {

	void evaluateAuthorizationRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) 
			throws AuthorizationFlowException, OAuthSystemException, IOException, ServletException, ResponseBuilderException;
	
	void sendAuthorizationReponse(HttpServletRequest request, HttpServletResponse response, 
			IPendingClientToken clientAuth, IRegisteredClientApp clientApp, String state) 
					throws OAuthSystemException, IOException, ResponseBuilderException;
	
	void sendErrorResponse(OAuthProblemException ex,
			HttpServletResponse response, String redirectUri) throws OAuthSystemException, IOException;

	void sendAuthorizationReponse(HttpServletRequest request, HttpServletResponse response,
			ResponseType reqResponseType, IRegisteredClientApp regClientApp, IAuthorizedClientApp authorizedClientApp, String state)
			throws OAuthSystemException, IOException, OAuthProblemException, OAuth2ProtocolException, ResponseBuilderException;	
}
