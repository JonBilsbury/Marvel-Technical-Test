package com.marvel.demo.services;

import com.marvel.demo.config.TranslateAPIConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranslateServiceTest
{
	@Mock
	TranslateAPIConfiguration translateAPI;
	WebClient webClient;

	TranslateService classUnderTest;

	@Mock
	private ExchangeFunction exchangeFunction;

	@BeforeEach
	void init()
	{
		webClient = WebClient.builder()
				.exchangeFunction(exchangeFunction)
				.build();

		classUnderTest = new TranslateService(webClient, translateAPI);
	}
	@Test
	public void testGetCharacterById()
	{
		when(translateAPI.getKey()).thenReturn("Key");
		when(translateAPI.getEndpoint()).thenReturn("Url");

		when(exchangeFunction.exchange(any(ClientRequest.class)))
				.thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK)
						.header("content-type", "application/json")
						.body("{\"data\": {\"translations\": [{\"translatedText\": \"Wort\"}]}}")
						.build()));

		Mono<String> source = classUnderTest.translateString("Word", "de");

		StepVerifier.create(source)
				.assertNext(result -> {
					assertEquals("Wort", result);
				}).verifyComplete();
	}
}