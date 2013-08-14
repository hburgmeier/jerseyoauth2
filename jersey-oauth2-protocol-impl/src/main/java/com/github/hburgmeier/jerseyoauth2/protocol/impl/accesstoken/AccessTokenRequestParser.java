package com.github.hburgmeier.jerseyoauth2.protocol.impl.accesstoken;

import java.util.EnumSet;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;
import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ParseException;
import com.github.hburgmeier.jerseyoauth2.api.types.GrantType;
import com.github.hburgmeier.jerseyoauth2.api.types.ParameterStyle;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ClientSecretExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.ScopeParser;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor.CombinedExtractor;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.oauth2.Constants;

public class AccessTokenRequestParser {

	private static final EnumSet<ParameterStyle> SUPPORTED_STYLES = EnumSet.of(ParameterStyle.BODY, ParameterStyle.QUERY);
	
	private final ScopeParser scopeParser = new ScopeParser();
	
	private final CombinedExtractor grantTypeExtractor = new CombinedExtractor(Constants.GRANT_TYPE, SUPPORTED_STYLES);
	private final CombinedExtractor clientIdExtractor = new CombinedExtractor(Constants.CLIENT_ID, SUPPORTED_STYLES);
	private final CombinedExtractor codeExtractor = new CombinedExtractor(Constants.CODE, SUPPORTED_STYLES);
	private final CombinedExtractor redirectUriExtractor = new CombinedExtractor(Constants.REDIRECT_URI, SUPPORTED_STYLES);
	private final CombinedExtractor refreshTokenExtractor = new CombinedExtractor(Constants.REFRESH_TOKEN, SUPPORTED_STYLES);
	private final CombinedExtractor scopeExtractor = new CombinedExtractor(Constants.SCOPE, SUPPORTED_STYLES);
	
	public AbstractTokenRequest parse(IHttpRequest request, boolean enableAuthorizationHeader) throws OAuth2ParseException
	{
		String grantTypeString = grantTypeExtractor.extractValue(request);
		if (StringUtils.isEmpty(grantTypeString)) {
			throw new OAuth2ParseException("Missing grant_type", null);
		}
		GrantType grantType = GrantType.parse(grantTypeString);
		
		if (grantType == GrantType.REFRESH_TOKEN) {
			return parseRefreshRequest(request, grantType, enableAuthorizationHeader);
		}
		else {
			return parseAuthCodeRequest(request, grantType, enableAuthorizationHeader);
		}
	}

	protected AuthCodeAccessTokenRequest parseAuthCodeRequest(IHttpRequest request, GrantType grantType, boolean enableAuthorizationHeader) {
		String clientId = clientIdExtractor.extractValue(request);
		String code = codeExtractor.extractValue(request);
		String redirectUri = redirectUriExtractor.extractValue(request);
		
		ClientSecretExtractor clientSecretExtractor = new ClientSecretExtractor(enableAuthorizationHeader);
		String clientSecret = clientSecretExtractor.extractValue(request);
		
		return new AuthCodeAccessTokenRequest(grantType, clientId, clientSecret, code, redirectUri);
	}

	protected RefreshTokenRequest parseRefreshRequest(IHttpRequest request, GrantType grantType, boolean enableAuthorizationHeader) {
		String refreshToken = refreshTokenExtractor.extractValue(request);
		String scope = scopeExtractor.extractValue(request);
		Set<String> scopes = scopeParser.parseScope(scope);
		
		String clientId = clientIdExtractor.extractValue(request);
		ClientSecretExtractor clientSecretExtractor = new ClientSecretExtractor(enableAuthorizationHeader);
		String clientSecret = clientSecretExtractor.extractValue(request);
		
		return new RefreshTokenRequest(grantType, clientId, clientSecret, refreshToken, scopes);
	}
	
}
