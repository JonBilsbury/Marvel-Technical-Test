package com.marvel.demo.services;

import com.marvel.demo.model.Result;
import com.marvel.demo.config.CacheConfiguration;
import com.marvel.demo.config.MarvelAPIConfiguration;
import com.marvel.demo.utils.MarvelUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.ExchangeFunction;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarvelServiceTest
{
	@Mock
	MarvelUtils marvelUtils;
	@Mock
	MarvelAPIConfiguration marvelAPI;
	@Mock
	CacheManager cacheManager;
	WebClient webClient;

	@Mock
	private ExchangeFunction exchangeFunction;

	@Mock
	private Cache cache;

	MarvelService marvelService;

	@BeforeEach
	void init()
	{
		webClient = WebClient.builder()
				.exchangeFunction(exchangeFunction)
				.build();

		marvelService = new MarvelService(marvelUtils, marvelAPI, webClient, cacheManager);
	}

	void initMarvelConfig()
	{
		when(marvelAPI.getUrl()).thenReturn("url");
	}

	void initCache()
	{
		when(cacheManager.getCache(CacheConfiguration.CacheType.MARVEL_DATA_CACHE.name())).thenReturn(cache);
	}

	@Test
	public void testGetAllCharacterIds_EmptyCache()
	{
		initMarvelConfig();
		initCache();
		when(cache.get(any(), eq(List.class))).thenReturn(null);
		when(exchangeFunction.exchange(any(ClientRequest.class)))
				.thenReturn(buildMockResponse());

		Mono<List<Result>> source = marvelService.getAllCharacterIds();

		StepVerifier.create(source)
				.assertNext(results -> {
					assertEquals(1, results.size());
				}).verifyComplete();
	}

	@Test
	public void testGetAllCharacterIds_InCache()
	{
		initCache();
		List<Result> mockedResults = new ArrayList<>();
		mockedResults.add(new Result());
		when(cache.get(any(), eq(List.class))).thenReturn(mockedResults);

		Mono<List<Result>> source = marvelService.getAllCharacterIds();

		StepVerifier.create(source)
				.assertNext(results -> {
					assertEquals(1, results.size());
				}).verifyComplete();
	}

	private Mono<ClientResponse> buildMockResponse() {
		return Mono.just(ClientResponse.create(HttpStatus.OK)
				.header("content-type", "application/json")
				.body("{\"data\": {\"total\": 1, \"results\": [{\"id\": 1}]}}")
				.build());
	}

	@Test
	public void testGetCharacterById()
	{
		initMarvelConfig();
		when(exchangeFunction.exchange(any(ClientRequest.class)))
				.thenReturn(Mono.just(ClientResponse.create(HttpStatus.OK)
						.header("content-type", "application/json")
						.body("{\"data\": {\"total\": 1, \"results\": [{\"id\": 1,\"name\": \"3-D Man\",\"description\": \"\",\"thumbnail\": {\"path\": \"http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784\",\"extension\": \"jpg\"}}]}}")
						.build()));

		Mono<Result> source = marvelService.getCharacterById("1");

		StepVerifier.create(source)
				.assertNext(result -> {
					assertNotNull(result);
					assertEquals("3-D Man", result.getName());
					assertEquals("", result.getDescription());
					assertEquals("http://i.annihil.us/u/prod/marvel/i/mg/c/e0/535fecbbb9784", result.getThumbnail().getPath());
				})
				.verifyComplete();
	}

	@Test
	public void testGetCharacterById_Error()
	{
		initMarvelConfig();
		when(exchangeFunction.exchange(any(ClientRequest.class)))
				.thenReturn(Mono.just(ClientResponse.create(HttpStatus.NOT_FOUND)
						.build()));

		Mono<Result> source = marvelService.getCharacterById("1");

		StepVerifier.create(source)
				.verifyError();
	}
}