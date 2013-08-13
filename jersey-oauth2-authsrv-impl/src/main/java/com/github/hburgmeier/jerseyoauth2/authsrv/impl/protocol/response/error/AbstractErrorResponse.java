package com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.error;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.github.hburgmeier.jerseyoauth2.api.protocol.OAuth2ProtocolException;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.AbstractOAuth2Response;
import com.github.hburgmeier.jerseyoauth2.authsrv.impl.protocol.response.ResponseFormat;
import com.github.hburgmeier.jerseyoauth2.protocol.impl.oauth2.Constants;

public abstract class AbstractErrorResponse extends AbstractOAuth2Response {

	protected Map<String, Object> errorEntity = new HashMap<>();
	
	public AbstractErrorResponse(int statusCode, ResponseFormat responseFormat, OAuth2ProtocolException ex)
	{
		super(statusCode, responseFormat);
		errorEntity.put(Constants.ERROR, ex.getErrorCode().getTechnicalCode());
		
		if (StringUtils.isNotEmpty(ex.getDescription()))
			errorEntity.put(Constants.ERROR_DESCRIPTION, ex.getDescription());

		if (StringUtils.isNotEmpty(ex.getErrorUri()))
			errorEntity.put(Constants.ERROR_URI, ex.getErrorUri());
		
		if (StringUtils.isNotEmpty(ex.getState()))
			errorEntity.put(Constants.STATE, ex.getState());
	}
	

}
