package com.marvel.demo.controller;

import com.marvel.demo.model.Result;
import com.marvel.demo.services.MarvelService;
import com.marvel.demo.services.TranslateService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class MarvelController {
	private final MarvelService marvelService;
	private final TranslateService translateService;


	@GetMapping("/characters")
	public Mono<List<String>> characters() {
		return marvelService.getAllCharacterIds()
			.flatMapIterable(a -> a)
			.map(Result::getId).collectList();
	}

	@GetMapping("/characters/{characterId}")
	public Mono<Result> singleCharacter(@PathVariable String characterId, @RequestParam(value = "language", required = false) Optional<String> languageCode) {
		return marvelService.getCharacterById(characterId)
			.flatMap(result -> languageCode.map(code -> Mono.zip(Mono.just(result), translateService.translateString(result.getDescription(), code)))
				.orElseGet(() -> Mono.zip(Mono.just(result), Mono.just(""))))
			.map(pair -> {
				Result result = pair.getT1();
				String a = pair.getT2();
				if (!a.isEmpty()) result.setDescription(a);
				return result;
			});
	}
}