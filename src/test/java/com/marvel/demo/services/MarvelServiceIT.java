package com.marvel.demo.services;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.marvel.demo.model.Result;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@Import({MarvelService.class})
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MarvelServiceIT {

	@Autowired
	MarvelService marvelService;

	WireMockServer wireMockServer;

	@BeforeEach
	void init()
	{
		wireMockServer = new WireMockServer(8090);
		wireMockServer.start();

		wireMockServer.stubFor(get(urlPathEqualTo("/characters"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBodyFile("wiremock/AllCharacterResponse.json")));
	}

	@AfterEach
	void teardown()
	{
		wireMockServer.stop();
	}

	@Test
	void testGetAllCharacters()
	{
		Mono<List<Result>> source = marvelService.getAllCharacterIds();

		StepVerifier.create(source)
				.assertNext(result -> {
					assertNotNull(result);
					assertEquals(10, result.size());
				})
				.verifyComplete();
	}

	@Test
	void testGetCharacterById()
	{
		wireMockServer.stubFor(get(urlPathEqualTo("/characters/1009718"))
				.willReturn(aResponse().withHeader("Content-Type", "application/json")
						.withStatus(200)
						.withBodyFile("wiremock/CharacterResponse.json")));

		Mono<Result> source = marvelService.getCharacterById("1009718");

		StepVerifier.create(source)
				.assertNext(result -> {
					assertNotNull(result);
					assertEquals("Wolverine", result.getName());
					assertEquals("http://i.annihil.us/u/prod/marvel/i/mg/2/60/537bcaef0f6cf", result.getThumbnail().getPath());
				})
				.verifyComplete();
	}
}