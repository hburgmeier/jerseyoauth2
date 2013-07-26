package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;

import com.github.hburgmeier.jerseyoauth2.api.user.IUser;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.user.IUserService;

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
