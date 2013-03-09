package com.burgmeier.jerseyoauth2.authsrv.jpa;

import java.util.UUID;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.authsrv.api.client.IClientAuthorization;

@Entity
@NamedQueries({
	@NamedQuery(name="findPendingByCode", query="select p from PendingClientAuthorization p where p.code = :code and " +
			"p.clientApp.clientApp.clientId = :clientId and " +
			"p.clientApp.clientApp.clientSecret = :clientSecret")
})
class PendingClientAuthorization implements IClientAuthorization {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private int id;
	
	private String code;
	
	@ManyToOne
	private AuthorizedClientApplication clientApp;
	
	public PendingClientAuthorization()
	{
		
	}

	public PendingClientAuthorization(AuthorizedClientApplication clientApp) {
		this.clientApp = clientApp;
		code = UUID.randomUUID().toString(); //TODO
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public AuthorizedClientApplication getClientApp() {
		return clientApp;
	}

	public void setClientApp(AuthorizedClientApplication clientApp) {
		this.clientApp = clientApp;
	}

	@Override
	public IAuthorizedClientApp getAuthorizedClient() {
		return clientApp;
	}
	
}
