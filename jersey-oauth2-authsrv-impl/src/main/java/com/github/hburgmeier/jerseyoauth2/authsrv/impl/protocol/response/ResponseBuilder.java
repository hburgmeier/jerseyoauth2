package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import java.net.URI;

import javax.servlet.http.HttpServletResponse;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IResponseBuilder;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken.AccessTokenEntityResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.accesstoken.AccessTokenRedirectResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.authcode.AuthCodeResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.error.AuthRequestErrorResponse;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.error.RequestTokenErrorResponse;

public class ResponseBuilder implements IResponseBuilder {

	@Override
	public IOAuth2Response buildRequestTokenErrorResponse(OAuth2ProtocolException ex)
			throws ResponseBuilderException {
		return new RequestTokenErrorResponse(HttpServletResponse.SC_BAD_REQUEST, ex);
	}

	@Override
	public IOAuth2Response buildAuthorizationRequestErrorResponse(OAuth2ProtocolException ex, URI redirectUrl) throws ResponseBuilderException {
		return new AuthRequestErrorResponse(HttpServletResponse.SC_MOVED_TEMPORARILY,
				redirectUrl, ex);
	}

	@Override
	public IOAuth2Response buildAuthorizationCodeResponse(String code, URI redirectUrl, String state)
			throws ResponseBuilderException {
		return new AuthCodeResponse(HttpServletResponse.SC_MOVED_TEMPORARILY, code,
				redirectUrl, state);
	}

	@Override
	public IOAuth2Response buildAccessTokenResponse(IAccessTokenInfo accessToken, String state)
			throws ResponseBuilderException {
		return new AccessTokenEntityResponse(HttpServletResponse.SC_OK,
				accessToken, state);
	}

	@Override
	public IOAuth2Response buildImplicitGrantAccessTokenResponse(IAccessTokenInfo accessToken, URI redirectUrl, String state) throws ResponseBuilderException {
		return new AccessTokenRedirectResponse(
				HttpServletResponse.SC_MOVED_TEMPORARILY, accessToken, redirectUrl, state);
	}
	
	@Override
	public IOAuth2Response buildForwardResponse(String relativeUrl) {
		return new ForwardOAuth2Response(relativeUrl);
	}
	
	@Override
	public IOAuth2Response buildUnauthorizedResponse(OAuth2ProtocolException ex) {
		return new UnauthorizedResponse();
	}
}
