package com.github.hburgmeier.jerseyoauth2.authsrv.api.protocol;

import java.io.OutputStream;

import com.github.hburgmeier.jerseyoauth2.api.protocol.ResponseBuilderException;

public interface IHttpContext {

	void forwardToPage(String relativeUrl) throws ResponseBuilderException;

	void setStatus(int statusCode);

	void setContentType(String contextType);

	void setHeader(String key, String value);

	OutputStream getResponseOutputStream() throws ResponseBuilderException;

	void addRequestAttribute(String key, Object value);

}
