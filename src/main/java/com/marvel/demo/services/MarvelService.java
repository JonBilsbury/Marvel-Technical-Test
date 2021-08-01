package com.marvel.demo.services;

import com.marvel.demo.model.MarvelResult;
import com.marvel.demo.model.Result;
import com.marvel.demo.config.CacheConfiguration;
import com.marvel.demo.config.MarvelAPIConfiguration;
import com.marvel.demo.utils.MarvelUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Repository;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;

@Slf4j
@Getter
@Repository
@AllArgsConstructor
public class MarvelService {
	private final MarvelUtils marvelUtil;
	private final MarvelAPIConfiguration marvelAPI;
	private final WebClient webClient;
	private final CacheManager cacheManager;

	@EventListener(ApplicationReadyEvent.class)
	public Mono<List<Result>> getAllCharacterIds() {
		log.debug("Fetching all character ids...");
		Cache cache = Optional.ofNullable(cacheManager.getCache(CacheConfiguration.CacheType.MARVEL_DATA_CACHE.name()))
				.orElseThrow(() -> new RuntimeException("Unable to find client cache"));

		return Optional.ofNullable(cache.get("characters", List.class))
				.map(data -> {
					log.debug("Character data found in cache.");
					return Mono.just((List<Result>) data);
				})
				.orElseGet(() -> {
					log.debug("No character data found in the cache");
					log.debug("Fetching all character IDs from api");
					WebClient webClient = getWebClient();
					return getMarvelResult(buildUri(marvelAPI.getUrl(), "0"))
							.doOnError(e -> log.error("Failed to retrieve IDs. {}", e.getMessage(), e))
							.map(marvelResult -> Integer.parseInt(marvelResult.getData().getTotal())) // maybe use apache.pair
							.flatMapIterable(totalCount -> {
								List<URI> uris = new ArrayList<>();
								IntStream.range(0, (totalCount + 99) / 100)
										.map(number -> number * 100)
										.forEach(integer -> uris.add(buildUri(marvelAPI.getUrl(), Integer.toString(integer))));
								return uris;
							}).parallel()
							.runOn(Schedulers.boundedElastic())
							.flatMap(uri -> getMarvelResult(uri)
									.doOnError(e -> log.error("Failed to retrieve IDs. {}", e.getMessage(), e)))
							.map(marvelResult -> marvelResult.getData().getResults()) // Flux<List<Result>> -> Mono<List<List<Result>>
							.map(List::stream)
							.sequential()
							.flatMap(Flux::fromStream)
							.collectList()
							.doOnSuccess(results -> {
								log.debug("successfully retrieved all marvel ids");
								cache.put("characters", results);
							});
				});
	}

	public Mono<Result> getCharacterById(String characterId)
	{
		URI uri = UriComponentsBuilder.fromUriString(marvelAPI.getUrl() + "/" + characterId)
				.queryParams(marvelUtil.buildParams())
				.build()
				.toUri();

		return getMarvelResult(uri)
				.doOnError(e -> log.error("Character doesn't exist with the ID {}", characterId, e))
				.doOnSuccess(data -> log.debug("Character result has been successfully fetched for id {}", characterId))
				.map(marvelResult -> marvelResult.getData().getResults().get(0));
	}

	private Mono<MarvelResult> getMarvelResult(final URI uri) {
		return webClient.get()
				.uri(uri)
				.retrieve()
				.bodyToMono(MarvelResult.class);
	}
	private URI buildUri(String uri, String offset) {
		return UriComponentsBuilder.fromUriString(uri)
				.queryParams(marvelUtil.buildParams())
				.queryParam("offset", offset)
				.build()
				.toUri();
	}
}
