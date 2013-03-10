package com.burgmeier.jerseyoauth2.authsrv.api.user;

import com.burgmeier.jerseyoauth2.api.user.IUser;

public interface IUserStorageService {

	void saveUser(IUser user) throws UserStorageServiceException;
	
	IUser loadUser(String userName) throws UserStorageServiceException;
	
}
