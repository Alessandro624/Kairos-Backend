package it.unical.demacs.informatica.KairosBackend.config.filter;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.AbstractRequestLoggingFilter;

//TODO(optional, maybe) define filters in a configuration file as beans for more flexibility
/*
* In Spring Boot moderno, FilterRegistrationBean
* è la scelta consigliata per registrare filtri.
* È più flessibile, più integrata nel contesto Spring, e evita problemi di doppia registrazione o comportamenti inattesi.
*
* AbstractRequestLoggingFilter makes easier logging, offering beforeRequest and afterRequest methods.
* */
@Component
@Order(1)
@Slf4j
public class LoggingFilter extends AbstractRequestLoggingFilter {

    public LoggingFilter() {
        setIncludeClientInfo(true);
        setIncludePayload(true);
        setIncludeHeaders(true);
        setIncludeQueryString(true);
    }

    @Override
    protected void beforeRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        log.info(">>> Incoming request: [{} {}] from [{}]\nHeaders: {}\nQuery: {}\nPayload: {}",
                request.getMethod(),
                request.getRequestURI(),
                request.getRemoteAddr(),
                request.getHeaderNames(),
                request.getQueryString(),
                message);
    }

    @Override
    protected void afterRequest(@NonNull HttpServletRequest request, @NonNull String message) {
        log.info("<<< Request has been handled: {}", message);
    }
}
