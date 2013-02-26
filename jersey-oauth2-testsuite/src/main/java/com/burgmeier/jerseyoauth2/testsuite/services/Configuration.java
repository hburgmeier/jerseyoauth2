package com.burgmeier.jerseyoauth2.testsuite.services;

import com.burgmeier.jerseyoauth2.api.IConfiguration;

public class Configuration implements IConfiguration {

	@Override
	public long getTokenExpiration() {
		return 3600;
	}

}
