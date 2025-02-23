package com.ezybytes.account.config;

import feign.codec.ErrorDecoder;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.StatusCode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignTracingConfig {

    @Bean
    public ErrorDecoder feignErrorDecoder() {
        return (methodKey, response) -> {
            Span span = Span.current();
            span.setStatus(StatusCode.ERROR);
            span.recordException(new RuntimeException("Service call failed: " + methodKey));
            return new RuntimeException("Failed request to " + methodKey + " with status " + response.status());
        };
    }
}