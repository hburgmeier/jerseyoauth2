package com.burgmeier.jerseyoauth2.impl.services;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import com.burgmeier.jerseyoauth2.api.user.IUser;
import com.burgmeier.jerseyoauth2.api.user.IUserService;

public class DefaultPrincipalUserService implements IUserService {

	@Override
	public IUser getCurrentUser(HttpServletRequest request) {
		return new PrincipalUser(request.getUserPrincipal());
	}
	
	private static class PrincipalUser implements IUser {

		private Principal principal;
		
		public PrincipalUser(Principal userPrincipal) {
			principal = userPrincipal;
		}

		@Override
		public String getName() {
			return principal.getName();
		}

		@Override
		public boolean isUserInRole(String role) {
			return false;
		}
		
	}

}
