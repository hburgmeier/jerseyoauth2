package com.github.hburgmeier.jerseyoauth2.authsrv.jpa;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.xml.bind.annotation.XmlRootElement;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.IAccessTokenInfo;

@Entity
@NamedQueries({
	@NamedQuery(name="findTokenEntityByRefreshToken", query="select te from TokenEntity te where te.refreshToken = :refreshToken"),
	@NamedQuery(name="findTokenEntityByUsername", query="select te from TokenEntity te where te.clientApp.username = :username")
})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
@XmlRootElement
class TokenEntity implements IAccessTokenInfo {

	@Id
	private String accessToken;
	private String refreshToken;
	private long expiresIn;
	private long validUntil;

	@ManyToOne(fetch=FetchType.EAGER)
	private AuthorizedClientApplication clientApp;
	
	public TokenEntity()
	{
		
	}
	
	public TokenEntity(String accessToken, String refreshToken, AuthorizedClientApplication clientApp, long expiresIn, long validUntil)
	{
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.clientApp = clientApp;
		this.expiresIn = expiresIn;
		this.validUntil = validUntil;
	}
	
	@Override
	public IAuthorizedClientApp getClientApp() {
		return clientApp;
	}

	@Override
	public IUser getUser() {
		return clientApp.getAuthorizedUser();
	}

	@Override
	public Set<String> getAuthorizedScopes() {
		return clientApp.getAuthorizedScopes();
	}

	@Override
	public String getExpiresIn() {
		return Long.toString(expiresIn);
	}

	@Override
	public String getRefreshToken() {
		return refreshToken;
	}

	@Override
	public String getAccessToken() {
		return accessToken;
	}

	@Override
	public void updateTokens(String newAccessToken, String newRefreshToken) {
		setAccessToken(newAccessToken);
		setRefreshToken(newRefreshToken);
	}

	@Override
	public boolean isExpired() {
		return System.currentTimeMillis()>validUntil;
	}
	
	@Override
	public String getClientId() {
		return this.clientApp.getClientId();
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void setExpiresIn(long expiresIn) {
		this.expiresIn = expiresIn;
	}

	public void setClientApp(AuthorizedClientApplication clientApp) {
		this.clientApp = clientApp;
	}

	public long getValidUntil() {
		return validUntil;
	}
	
}
