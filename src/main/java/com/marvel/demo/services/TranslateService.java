package com.marvel.demo.services;

import com.marvel.demo.config.TranslateAPIConfiguration;
import com.marvel.demo.translation.TranslateResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;

import java.net.URI;

@Slf4j
@Repository
@AllArgsConstructor
public class TranslateService {
	private final WebClient webClient;
	private final TranslateAPIConfiguration translateAPI;

	public Mono<String> translateString(String stringToTranslate, String languageCode) {
		URI uri = UriComponentsBuilder.fromUriString(translateAPI.getEndpoint())
				.queryParam("key", translateAPI.getKey())
				.queryParam("q", stringToTranslate)
				.queryParam("source", "en")
				.queryParam("target", languageCode)
				.build()
				.toUri();

		return webClient.post()
				.uri(uri)
				.retrieve()
				.bodyToMono(TranslateResponse.class)
				.doOnError(e -> log.error("Error occurred on translating the string {}", e.getMessage(), e))
				.doOnSuccess(data -> log.debug("Description has successfully been translated {}", data))
				.map(translateResponse -> translateResponse.getData().getTranslations().get(0).getTranslatedText());
	}
}