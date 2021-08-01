package com.marvel.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class TranslateAPIConfiguration
{
	@Bean
	public TranslateAPI translateAPI()
	{
		return new TranslateAPI();
	}
}