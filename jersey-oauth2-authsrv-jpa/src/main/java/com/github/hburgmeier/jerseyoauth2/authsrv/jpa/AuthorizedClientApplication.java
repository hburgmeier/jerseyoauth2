package com.github.hburgmeier.jerseyoauth2.authsrv.jpa;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import com.github.hburgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;

@Entity
@NamedQueries({
	@NamedQuery(name="findAuthorizedClient", query="select ac from AuthorizedClientApplication ac where ac.clientApp.clientId = :clientId and ac.username = :username")
})
public class AuthorizedClientApplication implements IAuthorizedClientApp {

	@Id
	private String id;
	
	private String username;
	
	@ManyToOne(fetch=FetchType.EAGER)
	private RegisteredClient clientApp;
	
	@ElementCollection(fetch=FetchType.EAGER)
	private Set<String> scopes = new HashSet<>();

	@Transient
	private IUser user;

	public AuthorizedClientApplication()
	{
	}
	
	public AuthorizedClientApplication(RegisteredClient clientApp, IUser user, Set<String> scopes)
	{
		this.clientApp = clientApp;
		this.user = user;
		this.scopes = new HashSet<>(scopes);
		
		this.username = user.getName();
		this.id = UUID.randomUUID().toString();
	}
	
	@Override
	public String getClientId() {
		return clientApp.getClientId();
	}

	@Override
	public IUser getAuthorizedUser() {
		return user;
	}

	@Override
	public Set<String> getAuthorizedScopes() {
		return scopes;
	}

	@Override
	public boolean isClientSecretValid(String clientSecret) {
		return clientApp.getClientSecret().equals(clientSecret);
	}
	
	@Override
	public String getCallbackUrl() {
		return clientApp.getCallbackUrl();
	}
	

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public RegisteredClient getClientApp() {
		return clientApp;
	}

	public void setClientApp(RegisteredClient clientApp) {
		this.clientApp = clientApp;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}
	
	void setAuthorizedUser(IUser user)
	{
		this.user = user;
	}

	public Set<String> getScopes() {
		return scopes;
	}

	public void setScopes(Set<String> scopes) {
		this.scopes = scopes;
	}
	
}
