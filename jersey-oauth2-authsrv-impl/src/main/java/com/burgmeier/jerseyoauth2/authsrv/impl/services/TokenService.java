package com.burgmeier.jerseyoauth2.authsrv.impl.services;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientAuthorization;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientService;
import com.burgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenStorageService;
import com.burgmeier.jerseyoauth2.authsrv.api.token.ITokenGenerator;
import com.burgmeier.jerseyoauth2.authsrv.api.token.ITokenService;
import com.google.inject.Inject;

public class TokenService implements ITokenService {

	private final IAccessTokenStorageService accessTokenService;
	private final IClientService clientService;
	private final ITokenGenerator tokenGenerator;
	
	@Inject
	public TokenService(final IAccessTokenStorageService accessTokenService, final IClientService clientService, final ITokenGenerator tokenGenerator)
	{
		this.accessTokenService = accessTokenService;
		this.clientService = clientService;
		this.tokenGenerator = tokenGenerator;
		
	}
	
	@Override
	public void issueToken(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		try {
			try {
				OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

				if (oauthRequest.getGrantType().equals(GrantType.REFRESH_TOKEN.toString())) {

					String refreshToken = oauthRequest.getRefreshToken();

					try {
						IAccessTokenInfo oldTokenInfo = accessTokenService.getTokenInfoByRefreshToken(refreshToken);

						String newAccessToken = tokenGenerator.createAccessToken();
						String newRefreshToken = tokenGenerator.createRefreshToken();

						IAccessTokenInfo accessTokenInfo = accessTokenService.refreshToken(
								oldTokenInfo.getAccessToken(), newAccessToken, newRefreshToken);

						sendTokenResponse(response, accessTokenInfo);
					} catch (InvalidTokenException e) {
						throw OAuthProblemException.error("token_invalid", "token is invalid");
					}

				} else if (oauthRequest.getGrantType().equals(GrantType.AUTHORIZATION_CODE.toString())) {

					IClientAuthorization pendingClientToken = clientService.findPendingClientToken(oauthRequest.getClientId(),
							oauthRequest.getClientSecret(), oauthRequest.getCode());
					if (pendingClientToken == null) {
						throw OAuthProblemException.error("client_not_auth", "client not authorized");
					}

					String accessToken = tokenGenerator.createAccessToken();
					String refreshToken = tokenGenerator.createRefreshToken();

					IAccessTokenInfo accessTokenInfo = accessTokenService.issueToken(accessToken, refreshToken,
							pendingClientToken.getAuthorizedClient());

					sendTokenResponse(response, accessTokenInfo);
				}

				// if something goes wrong
			} catch (OAuthProblemException ex) {
				sendErrorResponse(response, ex);
			}
		} catch (OAuthSystemException e) {
			throw new ServletException(e);
		}
	}

	@Override
	public void sendErrorResponse(HttpServletResponse response, OAuthProblemException ex) throws OAuthSystemException,
			IOException {
		OAuthResponse r = OAuthResponse.errorResponse(401).error(ex).buildJSONMessage();

		response.setStatus(r.getResponseStatus());

		PrintWriter pw = response.getWriter();
		pw.print(r.getBody());
		pw.flush();
		pw.close();
	}

	@Override
	public void sendTokenResponse(HttpServletResponse response, IAccessTokenInfo accessTokenInfo)
			throws OAuthSystemException, IOException {
		OAuthResponse r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(accessTokenInfo.getAccessToken()).setExpiresIn(accessTokenInfo.getExpiresIn())
				.setRefreshToken(accessTokenInfo.getRefreshToken()).buildJSONMessage();

		response.setStatus(r.getResponseStatus());
		PrintWriter pw = response.getWriter();
		pw.print(r.getBody());
		pw.flush();
		pw.close();
	}

}
