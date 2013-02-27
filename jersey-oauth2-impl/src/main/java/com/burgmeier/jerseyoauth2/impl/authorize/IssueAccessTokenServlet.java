package com.burgmeier.jerseyoauth2.impl.authorize;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.amber.oauth2.as.issuer.MD5Generator;
import org.apache.amber.oauth2.as.issuer.OAuthIssuer;
import org.apache.amber.oauth2.as.issuer.OAuthIssuerImpl;
import org.apache.amber.oauth2.as.request.OAuthTokenRequest;
import org.apache.amber.oauth2.as.response.OAuthASResponse;
import org.apache.amber.oauth2.common.exception.OAuthProblemException;
import org.apache.amber.oauth2.common.exception.OAuthSystemException;
import org.apache.amber.oauth2.common.message.OAuthResponse;
import org.apache.amber.oauth2.common.message.types.GrantType;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.client.IClientService;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenService;
import com.burgmeier.jerseyoauth2.api.token.InvalidTokenException;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class IssueAccessTokenServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final IAccessTokenService accessTokenService;
	private final IClientService clientService;
	private OAuthIssuer oauthIssuerImpl = new OAuthIssuerImpl(new MD5Generator());
	
	@Inject
	public IssueAccessTokenServlet(final IAccessTokenService accessTokenService, final IClientService clientService) {
		this.accessTokenService = accessTokenService;
		this.clientService = clientService;
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
			IOException {

		issueToken(request, response);
	}

	private void issueToken(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ServletException {
		try {
			try {
				OAuthTokenRequest oauthRequest = new OAuthTokenRequest(request);

				if (oauthRequest.getGrantType().equals(GrantType.REFRESH_TOKEN.toString())) {

					String refreshToken = oauthRequest.getRefreshToken();
					
					try {
						IAccessTokenInfo oldTokenInfo = accessTokenService.getTokenInfoByRefreshToken(refreshToken);

						String newAccessToken = oauthIssuerImpl.accessToken();
						String newRefreshToken = oauthIssuerImpl.refreshToken();
						
						IAccessTokenInfo accessTokenInfo = accessTokenService.refreshToken(oldTokenInfo.getAccessToken(), newAccessToken, newRefreshToken);
						
						sendTokenResponse(response, accessTokenInfo);
					} catch (InvalidTokenException e) {
						throw OAuthProblemException.error("token_invalid", "token is invalid");
					}
					
				} else if (oauthRequest.getGrantType().equals(GrantType.AUTHORIZATION_CODE.toString())) {

					IAuthorizedClientApp clientApp = clientService.findPendingClient(oauthRequest.getClientId(),
							oauthRequest.getClientSecret(), oauthRequest.getCode());
					if (clientApp == null) {
						throw OAuthProblemException.error("client_not_auth", "client not authorized");
					}
					
					String accessToken = oauthIssuerImpl.accessToken();
					String refreshToken = oauthIssuerImpl.refreshToken();

					IAccessTokenInfo accessTokenInfo = accessTokenService.issueToken(accessToken, refreshToken, clientApp);

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

	private void sendErrorResponse(HttpServletResponse response, OAuthProblemException ex) throws OAuthSystemException,
			IOException {
		OAuthResponse r = OAuthResponse.errorResponse(401).error(ex).buildJSONMessage();

		response.setStatus(r.getResponseStatus());

		PrintWriter pw = response.getWriter();
		pw.print(r.getBody());
		pw.flush();
		pw.close();
	}

	private void sendTokenResponse(HttpServletResponse response, IAccessTokenInfo accessTokenInfo) throws OAuthSystemException, IOException {
		OAuthResponse r = OAuthASResponse.tokenResponse(HttpServletResponse.SC_OK)
				.setAccessToken(accessTokenInfo.getAccessToken())
				.setExpiresIn(accessTokenInfo.getExpiresIn())
				.setRefreshToken(accessTokenInfo.getRefreshToken()).buildJSONMessage();

		response.setStatus(r.getResponseStatus());
		PrintWriter pw = response.getWriter();
		pw.print(r.getBody());
		pw.flush();
		pw.close();
	}

}
