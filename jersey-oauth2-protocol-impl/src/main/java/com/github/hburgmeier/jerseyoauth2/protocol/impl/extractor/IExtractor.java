package com.github.hburgmeier.jerseyoauth2.protocol.impl.extractor;

import com.github.hburgmeier.jerseyoauth2.api.protocol.IHttpRequest;

public interface IExtractor {

	String extractValue(IHttpRequest request);
	
}
