package com.ezybytes.account.config;

import feign.RequestInterceptor;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.SpanContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignTracingConfig {

    @Bean
    public RequestInterceptor tracingFeignInterceptor() {
        return template -> {
            Span currentSpan = Span.current();
            SpanContext spanContext = currentSpan.getSpanContext();

            if (spanContext.isValid()) {
                template.header("traceparent", spanContext.getTraceId());
                template.header("tracestate", spanContext.getTraceState().toString());
            }
        };
    }
}
