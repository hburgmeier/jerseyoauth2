package com.github.hburgmeier.jerseyoauth2.authsrv.api.authorization;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IAuthorizationRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IRegisteredClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.ui.AuthorizationFlowException;


public interface IAuthorizationService {

	IOAuth2Response evaluateAuthorizationRequest(HttpServletRequest request, HttpServletResponse response, ServletContext servletContext) 
			throws AuthorizationFlowException, IOException, ServletException, ResponseBuilderException;
	
	IOAuth2Response sendAuthorizationResponse(HttpServletRequest request,
			IAuthorizationRequest originalRequest, IRegisteredClientApp regClientApp, IAuthorizedClientApp authorizedClientApp)
			throws IOException, OAuth2ProtocolException, ResponseBuilderException;	
	
	IOAuth2Response sendErrorResponse(OAuth2ProtocolException ex,
			URI redirectUrl) throws ResponseBuilderException;	
}
