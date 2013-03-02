package com.burgmeier.jerseyoauth2.authsrv.api.user;

import javax.servlet.http.HttpServletRequest;

import com.burgmeier.jerseyoauth2.api.user.IUser;

public interface IUserService {

	IUser getCurrentUser(HttpServletRequest request);
	
}
