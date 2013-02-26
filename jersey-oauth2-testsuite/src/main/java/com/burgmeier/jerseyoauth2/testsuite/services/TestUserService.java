package com.burgmeier.jerseyoauth2.testsuite.services;

import javax.servlet.http.HttpServletRequest;

import com.burgmeier.jerseyoauth2.api.simple.SimpleUser;
import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.api.user.IUserService;

public class TestUserService implements IUserService {

	@Override
	public IUser getCurrentUser(HttpServletRequest request) {
		return new SimpleUser(request.getUserPrincipal()); 
	}

}
