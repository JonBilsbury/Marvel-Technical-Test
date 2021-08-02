package com.marvel.demo.controller;

import com.marvel.demo.model.Result;
import com.marvel.demo.services.MarvelService;
import com.marvel.demo.services.TranslateService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MarvelControllerTest
{
	@Mock
	MarvelService marvelService;
	@Mock
	TranslateService translateService;
	@InjectMocks
	MarvelController marvelController;

	@Test
	public void testCharacters()
	{
		List<Result> results = new ArrayList<>();
		Result result1 = new Result();
		result1.setId("1");
		results.add(result1);
		Result result2 = new Result();
		result2.setId("2");
		results.add(result2);

		when(marvelService.getAllCharacterIds()).thenReturn(Mono.just(results));

		Mono<List<String>> source = marvelController.characters();

		StepVerifier.create(source)
				.assertNext(testResult -> {
					assertEquals(2, testResult.size());
				}).verifyComplete();
	}

	@Test
	public void testSingleCharacter_NoLanguage()
	{
		Result result = new Result();
		result.setId("1");

		when(marvelService.getCharacterById(any())).thenReturn(Mono.just(result));

		Mono<Result> source = marvelController.singleCharacter(result.getId(), Optional.empty());

		StepVerifier.create(source)
				.assertNext(testResult -> {
					assertNotNull(testResult);
					assertEquals("1", testResult.getId());
				}).verifyComplete();
	}

	@Test
	public void testSingleCharacter_Language()
	{
		Result result = new Result();
		result.setId("1");
		result.setDescription("Word");

		when(marvelService.getCharacterById(any())).thenReturn(Mono.just(result));
		when(translateService.translateString(any(), any())).thenReturn(Mono.just("Wort"));
		Mono<Result> source = marvelController.singleCharacter(result.getId(), Optional.of("de"));

		StepVerifier.create(source)
				.assertNext(testResult -> {
					assertNotNull(testResult);
					assertEquals("1", testResult.getId());
					assertEquals("Wort", testResult.getDescription());
				}).verifyComplete();
	}
}