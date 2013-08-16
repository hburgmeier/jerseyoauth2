package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.util.UUID;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IClientIdGenerator;

public class UUIDClientIdGenerator implements IClientIdGenerator {

	@Override
	public String generateClientId() {
		return UUID.randomUUID().toString();
	}

	@Override
	public String generateClientSecret() {
		return UUID.randomUUID().toString();
	}

}
