package com.marvel.demo.controller;

import com.marvel.demo.character.CharacterList;
import com.marvel.demo.character.Result;
import com.marvel.demo.services.MarvelService;
import com.marvel.demo.services.TranslateService;
import org.junit.jupiter.api.Test;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MarvelControllerTest
{
	/*CharacterList characterList;
	List<Service> serviceList;

	MarvelController classUnderTest;

	@Test
	public void testCharacters()
	{
		List<String> characterIds = new ArrayList<>();
		characterIds.add("1");
		characterIds.add("2");
		characterList = new CharacterList(characterIds);

		classUnderTest = new MarvelController(characterList, new ArrayList<>());

		String[] testResult = classUnderTest.characters();

		assertArrayEquals(characterIds.toArray(new String[0]), testResult);
	}

	@Test
	public void testSingleCharacter_NoLanguage() throws UnsupportedEncodingException
	{
		String characterId = "1";
		Result result = new Result();

		MarvelSingleService marvelSingleService = mock(MarvelSingleService.class);
		when(marvelSingleService.getCharacterById(eq(characterId))).thenReturn(result);

		serviceList = new ArrayList<>();
		serviceList.add(marvelSingleService);

		classUnderTest = new MarvelController(characterList, serviceList);

		Result testResult = classUnderTest.singleCharacter(characterId, Optional.empty());

		assertEquals(result, testResult);
	}

	@Test
	public void testSingleCharacter_Language() throws UnsupportedEncodingException
	{
		String characterId = "1";
		String languageCode = "de";

		Result expectedResult = new Result();
		expectedResult.setDescription("Wort");

		Result result = new Result();
		result.setDescription("Word");

		MarvelSingleService marvelSingleService = mock(MarvelSingleService.class);
		when(marvelSingleService.getCharacterById(eq(characterId))).thenReturn(result);

		TranslateService translateService = mock(TranslateService.class);
		when(translateService.translateString(eq("Word"), eq("de"))).thenReturn("Wort");
		serviceList = new ArrayList<>();
		serviceList.add(marvelSingleService);
		serviceList.add(translateService);

		classUnderTest = new MarvelController(characterList, serviceList);

		Result testResult = classUnderTest.singleCharacter(characterId, Optional.of(languageCode));

		assertEquals(expectedResult.getDescription(), testResult.getDescription());
	}*/
}