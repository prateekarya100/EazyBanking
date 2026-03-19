package com.tomcat.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Optional;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator eazybankRouteConfig(RouteLocatorBuilder builder) {
		return builder.routes()
				.route(p -> p
						.path("/eazybank/accounts/**")
						.and()
						.method(HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
						.filters(f -> f
								.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-ResponseTime", LocalDateTime.now().toString())
								.addRequestHeader("X-Gateway", "EazyBank-GW")
								.addRequestParameter("source", "gateway")
								.circuitBreaker(c -> c
										.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contact-support")
								)
						)
						.uri("lb://ACCOUNTS")   // ✅ uppercase
				)
				.route(p -> p
						.path("/eazybank/cards/**")
						.and()
						.method(HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
						.filters(f -> f
								.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-ResponseTime", LocalDateTime.now().toString())
								.circuitBreaker(c -> c
										.setName("cardsCircuitBreaker")
										.setFallbackUri("forward:/contact-cards-support-team")
								)
						)
						.uri("lb://CARDS")     // ✅ uppercase
				)
				.route(p -> p
						.path("/eazybank/loans/**")
						.and()
						.method(HttpMethod.POST, HttpMethod.GET, HttpMethod.PUT, HttpMethod.DELETE)
						.filters(f -> f
								.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.circuitBreaker(c -> c
										.setName("loansCircuitBreaker")
										.setFallbackUri("forward:/contact-loans-support-team")
								)
								.retry(retryConfig -> retryConfig
										.setRetries(3)
										.setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, true)
								)
								.requestRateLimiter(api_rate -> api_rate
										.setRateLimiter(redisRateLimiter())
										.setKeyResolver(userKeyResolver())
								)
						)
						.uri("lb://LOANS")     // ✅ uppercase
				)
				.build();
	}

	@Bean
	public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
		return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
				.circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
				.timeLimiterConfig(TimeLimiterConfig.custom()
						.timeoutDuration(Duration.ofSeconds(10))  // ✅ increased from 4s
						.build())
				.build());
	}

	@Bean
	public KeyResolver userKeyResolver() {
		return exchange -> Mono.just(
				Optional.ofNullable(exchange.getRequest().getHeaders().getFirst("X-User-Id"))
						.orElse("anonymous")
		);
	}

	@Bean
	public RedisRateLimiter redisRateLimiter() {
		return new RedisRateLimiter(10, 20, 1);  // ✅ increased from 1,1,1
	}
}