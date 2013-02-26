package com.burgmeier.jerseyoauth2.api.user;

import javax.servlet.http.HttpServletRequest;

public interface IUserService {

	IUser getCurrentUser(HttpServletRequest request);
	
}
