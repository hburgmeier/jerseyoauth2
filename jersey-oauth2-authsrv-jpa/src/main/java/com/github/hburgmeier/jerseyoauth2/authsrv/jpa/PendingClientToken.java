package com.github.hburgmeier.jerseyoauth2.authsrv.jpa;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IPendingClientToken;

@Entity
@NamedQueries({
	@NamedQuery(name="findPendingByCode", query="select p from PendingClientToken p where p.code = :code and " +
			"p.clientApp.clientApp.clientId = :clientId and " +
			"p.clientApp.clientApp.clientSecret = :clientSecret")
})
@XmlRootElement
class PendingClientToken implements IPendingClientToken {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)	
	private int id;
	
	private String code;
	private long expiration;
	
	@ManyToOne
	private AuthorizedClientApplication clientApp;
	
	public PendingClientToken()
	{
		
	}

	public PendingClientToken(AuthorizedClientApplication clientApp, String code) {
		this.clientApp = clientApp;
		this.code = code;
		this.expiration = System.currentTimeMillis() + TEN_MINUTES;
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

	public long getExpiration() {
		return expiration;
	}

	public void setExpiration(long expiration) {
		this.expiration = expiration;
	}

	@Override
	public IAuthorizedClientApp getAuthorizedClient() {
		return clientApp;
	}
	
	@Override
	public boolean isExpired() {
		return System.currentTimeMillis() > expiration;
	}
}
