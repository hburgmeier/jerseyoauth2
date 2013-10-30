package com.github.hburgmeier.jerseyoauth2.api.protocol;

public interface IHttpRequest {

	String getMethod();
	
	String getHeaderField(String field);
	
	String getQueryParameter(String queryParameter);
	
	String getFormParameterValue(String field);
}
