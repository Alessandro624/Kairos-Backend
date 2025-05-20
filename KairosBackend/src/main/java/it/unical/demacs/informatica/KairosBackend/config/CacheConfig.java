package it.unical.demacs.informatica.KairosBackend.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Configuration
@EnableCaching
@EnableScheduling
@Slf4j
public class CacheConfig {
    public static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static final String CACHE_FOR_USER = "USER";

    @Bean("cacheManager")
    public CacheManager manager() {
        return new ConcurrentMapCacheManager(CACHE_FOR_USER);
    }

    @CacheEvict(allEntries = true, value = {CACHE_FOR_USER})
    @Scheduled(fixedDelay = 10 * 60 * 1000, initialDelay = 500)
    public void placeholderIdCacheEvict() {
        log.info("Flush Cache[{}] at [{}]", CACHE_FOR_USER, formatter.format(LocalDateTime.now()));
    }
}
