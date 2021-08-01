package com.marvel.demo.utils;

import com.marvel.demo.config.MarvelAPIConfiguration;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class MarvelUtils {
	private final MessageDigest messageDigest;
	private final MarvelAPIConfiguration marvelAPI;

	private String getTimestamp()
	{
		return String.valueOf(Instant.now().getEpochSecond());
	}

	private String buildHash(String timestamp)
	{
		String hashString = timestamp + marvelAPI.getPrivatekey() + marvelAPI.getPublickey();
		messageDigest.update(hashString.getBytes(StandardCharsets.UTF_8));
		return new BigInteger(1, messageDigest.digest()).toString(16);
	}

	public MultiValueMap<String, String> buildParams()
	{
		String timestamp = getTimestamp();
		String hash = buildHash(timestamp);
		MultiValueMap<String, String> urlParameters = new LinkedMultiValueMap<>();
		urlParameters.put("apikey", Collections.singletonList(marvelAPI.getPublickey()));
		urlParameters.put("ts", Collections.singletonList(timestamp));
		urlParameters.put("hash", Collections.singletonList(hash));
		urlParameters.put("limit", Collections.singletonList("100"));
		return urlParameters;
	}
}
