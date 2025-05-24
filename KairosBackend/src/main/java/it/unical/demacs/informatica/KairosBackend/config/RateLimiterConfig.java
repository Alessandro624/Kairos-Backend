package it.unical.demacs.informatica.KairosBackend.config;

import com.google.common.util.concurrent.RateLimiter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfig {
    @Value("${kairos.rate-limiter.permits-per-second}")
    private long requestsPerSecond;

    @Bean
    public RateLimiter rateLimiter() {
        return RateLimiter.create(requestsPerSecond);
    }
}
