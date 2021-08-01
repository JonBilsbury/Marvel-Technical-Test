package com.marvel.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Configuration
public class SecurityConfiguration
{
	@Bean
	public MessageDigest messageDigest() throws NoSuchAlgorithmException
	{
		return MessageDigest.getInstance("MD5");
	}
}