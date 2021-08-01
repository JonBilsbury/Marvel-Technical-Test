package com.marvel.demo.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.Ticker;
import org.springframework.cache.CacheManager;
import org.springframework.cache.caffeine.CaffeineCache;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.support.SimpleCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Configuration
@EnableCaching
public class CacheConfiguration {
	private static final int CACHE_SIZE = 1000;
	private static final int DAYS_TO_EXPIRE = 1;

	@Bean
	public CacheManager cacheManager(Ticker ticker) {
		final CaffeineCache clientCache = buildCache(CacheType.MARVEL_DATA_CACHE.name(), ticker, DAYS_TO_EXPIRE, TimeUnit.DAYS);
		final SimpleCacheManager manager = new SimpleCacheManager();
		manager.setCaches(List.of(clientCache));
		return manager;
	}

	private CaffeineCache buildCache(String name, Ticker ticker, int timeToExpire, TimeUnit timeUnit) {
		return new CaffeineCache(name, Caffeine.newBuilder()
				.expireAfterWrite(timeToExpire, timeUnit)
				.maximumSize(CACHE_SIZE)
				.ticker(ticker)
				.build());
	}

	@Bean
	public Ticker ticker() {
		return Ticker.systemTicker();
	}

	public enum CacheType {
		MARVEL_DATA_CACHE,
	}
}
