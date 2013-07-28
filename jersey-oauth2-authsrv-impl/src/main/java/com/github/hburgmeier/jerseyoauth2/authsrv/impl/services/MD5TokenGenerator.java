package com.github.hburgmeier.jerseyoauth2.authsrv.impl.services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.ITokenGenerator;
import com.github.hburgmeier.jerseyoauth2.authsrv.api.token.TokenGenerationException;

public class MD5TokenGenerator implements ITokenGenerator {

	@Override
	public String createAccessToken() throws TokenGenerationException {
		return generateValue();
	}

	@Override
	public String createRefreshToken() throws TokenGenerationException {
		return generateValue();
	}

	@Override
	public String createAuthenticationCode() throws TokenGenerationException {
		return generateValue();
	}
	
	protected String generateValue() throws TokenGenerationException
	{
		try {
			return calculateHexMD5(UUID.randomUUID().toString());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new TokenGenerationException(e);
		}
	}
	
	protected String calculateHexMD5(String value) throws NoSuchAlgorithmException, UnsupportedEncodingException
	{
        MessageDigest md = MessageDigest.getInstance("MD5");
        byte[] mdbytes = md.digest(value.getBytes("UTF-8"));
 
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < mdbytes.length; i++) {
          sb.append(Integer.toString((mdbytes[i] & 0xff) + 0x100, 16).substring(1));
        }
 
    	return sb.toString();
	}

}
