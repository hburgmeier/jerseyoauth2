package com.github.hburgmeier.jerseyoauth2.api.token;

import java.util.Set;

import com.github.hburgmeier.jerseyoauth2.api.client.IAuthorizedClientApp;
import com.github.hburgmeier.jerseyoauth2.api.user.IUser;

public interface IAccessTokenInfo {

	IUser getUser();

	IAuthorizedClientApp getClientApp();

	Set<String> getAuthorizedScopes();

}
