package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;

public class OAuth2ErrorResponse extends AbstractOAuth2Response {

	protected Map<String, Object> errorEntity = new HashMap<>();
	
	public OAuth2ErrorResponse(int statusCode, ResponseFormat responseFormat, OAuth2ProtocolException ex)
	{
		super(statusCode, responseFormat);
		errorEntity.put("error", ex.getErrorCode().getTechnicalCode());
		
		if (StringUtils.isNotEmpty(ex.getDescription()))
			errorEntity.put("error_description", ex.getDescription());

		if (StringUtils.isNotEmpty(ex.getErrorUri()))
			errorEntity.put("error_description", ex.getErrorUri());
		
		if (StringUtils.isNotEmpty(ex.getState()))
			errorEntity.put("state", ex.getState());
	}
	
	@Override
	public void render(HttpServletResponse response) throws ResponseBuilderException {
		super.render(response);
		
		renderJson(errorEntity, response);
	}
}
