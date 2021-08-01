package com.marvel.demo.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "marvel.api")
public class MarvelAPIConfiguration
{
	private String url;
	private String publickey;
	private String privatekey;
}