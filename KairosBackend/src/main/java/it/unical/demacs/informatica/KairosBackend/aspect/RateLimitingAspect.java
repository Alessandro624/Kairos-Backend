package it.unical.demacs.informatica.KairosBackend.aspect;

import com.google.common.util.concurrent.RateLimiter;
import it.unical.demacs.informatica.KairosBackend.exception.RateLimitExceededException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RestController;

@Aspect
@Component
@RequiredArgsConstructor
@Slf4j
public class RateLimitingAspect {
    private final RateLimiter rateLimiter;

    @Pointcut("@within(controller)")
    public void restControllerMethods(RestController controller) {
    }

    @Before(value = "restControllerMethods(controller)", argNames = "joinPoint,controller")
    public void applyRateLimit(JoinPoint joinPoint, RestController controller) {
        log.debug("Applying rate-limiting for rest controller {} with parameters {}", controller, joinPoint.getArgs());
        if (!rateLimiter.tryAcquire()) {
            throw new RateLimitExceededException("Rate limit exceeded");
        }
    }
}
