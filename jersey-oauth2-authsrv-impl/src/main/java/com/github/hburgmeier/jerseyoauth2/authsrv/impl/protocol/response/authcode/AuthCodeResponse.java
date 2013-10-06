package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.authcode;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol.IHttpContext;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.AbstractOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.oauth2.Constants;

public class AuthCodeResponse extends AbstractOAuth2Response {

	protected final Map<String, Object> codeEntity = new HashMap<>();
	protected final URI redirectUrl;
	
	public AuthCodeResponse(int statusCode, String code, URI redirectUrl, String state) {
		super(statusCode, ResponseFormat.QUERY);
		this.redirectUrl = redirectUrl;
		
		codeEntity.put(Constants.CODE, code);
		if (StringUtils.isNotEmpty(state)) {
			codeEntity.put(Constants.STATE, state);
		}
	}

	@Override
	public void render(IHttpContext context) throws ResponseBuilderException {
		super.render(context);
		
		renderRedirect(codeEntity, redirectUrl, context);
	}
}
