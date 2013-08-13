package com.github.hburgmeier.jerseyoauth2.sample.openid.services;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

public class MD5Generator {
	
	public String generateValue() throws GenerationException
	{
		try {
			return calculateHexMD5(UUID.randomUUID().toString());
		} catch (NoSuchAlgorithmException | UnsupportedEncodingException e) {
			throw new GenerationException(e);
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
