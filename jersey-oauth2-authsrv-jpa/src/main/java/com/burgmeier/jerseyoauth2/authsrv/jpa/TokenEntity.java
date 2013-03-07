package com.burgmeier.jerseyoauth2.authsrv.jpa;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Transient;

import com.burgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.burgmeier.jerseyoauth2.api.token.IAccessTokenInfo;
import com.burgmeier.jerseyoauth2.api.user.IUser;

@Entity
@NamedQueries({
	@NamedQuery(name="findTokenInfoByRefreshToken", query="select te from TokenEntity te where te.refreshToken = :refreshToken")
})
class TokenEntity implements IAccessTokenInfo {

	@Id
	private String accessToken;
	private String refreshToken;
	
	@Transient
	private IAuthorizedClientApp clientApp;
	
	public TokenEntity()
	{
		
	}
	
	public TokenEntity(String accessToken, String refreshToken, IAuthorizedClientApp clientApp)
	{
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
		this.clientApp = clientApp;
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
		return null; //TODO
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
	
}
