package com.github.hburgmeier.jerseyoauth2.authsrv.api.user;

import javax.servlet.http.HttpServletRequest;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;

public interface IUserService {

	IUser getCurrentUser(HttpServletRequest request);
	
}
