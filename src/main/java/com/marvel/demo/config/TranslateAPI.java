package com.marvel.demo.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;

@Getter
public class TranslateAPI
{
	@Value("${translator.api.endpoint}")
	private String url;
	@Value("${translator.api.key}")
	private String key;
}