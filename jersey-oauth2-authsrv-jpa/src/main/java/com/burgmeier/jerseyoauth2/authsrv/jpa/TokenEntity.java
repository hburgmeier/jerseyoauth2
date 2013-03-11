package com.burgmeier.jerseyoauth2.authsrv.jpa;

import java.util.Set;

import javax.persistence.Cacheable;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.user.IUser;

@Entity
@NamedQueries({
	@NamedQuery(name="findTokenEntityByRefreshToken", query="select te from TokenEntity te where te.refreshToken = :refreshToken"),
	@NamedQuery(name="findTokenEntityByUsername", query="select te from TokenEntity te where te.clientApp.username = :username")
})
@Cacheable
@Cache(usage=CacheConcurrencyStrategy.READ_WRITE)
class TokenEntity implements IAccessTokenInfo {

	@Id
	private String accessToken;
	private String refreshToken;
	private long expiredIn;

	@ManyToOne(fetch=FetchType.EAGER)
	private AuthorizedClientApplication clientApp;
	
	public TokenEntity()
	{
		
	}
	
	public TokenEntity(String accessToken, String refreshToken, AuthorizedClientApplication clientApp, long expiredIn)
	{
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.clientApp = clientApp;
		this.expiredIn = expiredIn;
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
		return Long.toString(expiredIn);
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

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public void setExpiredIn(long expiredIn) {
		this.expiredIn = expiredIn;
	}

	public void setClientApp(AuthorizedClientApplication clientApp) {
		this.clientApp = clientApp;
	}

	
	
}
