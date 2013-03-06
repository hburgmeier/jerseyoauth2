package com.burgmeier.jerseyoauth2.authsrv.jpa;

import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;

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
	
	public TokenEntity()
	{
		
	}
	
	public TokenEntity(String accessToken, String refreshToken)
	{
		this.accessToken = accessToken;
		this.refreshToken = refreshToken;
	}
	
	@Override
	public IAuthorizedClientApp getClientApp() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IUser getUser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getAuthorizedScopes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getExpiresIn() {
		// TODO Auto-generated method stub
		return null;
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
		// TODO Auto-generated method stub
		
	}

}
