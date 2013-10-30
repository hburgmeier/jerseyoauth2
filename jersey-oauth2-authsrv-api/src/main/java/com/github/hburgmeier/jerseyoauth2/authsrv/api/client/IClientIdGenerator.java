package com.github.hburgmeier.jerseyoauth2.authsrv.api.client;

public interface IClientIdGenerator {

	String generateClientId();

	String generateClientSecret();

}
