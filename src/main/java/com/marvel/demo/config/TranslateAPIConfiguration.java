package com.marvel.demo.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "translator.api")
public class TranslateAPIConfiguration
{
	@Value("${translator.api.endpoint}")
	private String endpoint;
	@Value("${translator.api.key}")
	private String key;
}