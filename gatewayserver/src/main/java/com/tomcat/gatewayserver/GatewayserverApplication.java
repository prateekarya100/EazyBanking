package com.tomcat.gatewayserver;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.timelimiter.TimeLimiterConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JCircuitBreakerFactory;
import org.springframework.cloud.circuitbreaker.resilience4j.Resilience4JConfigBuilder;
import org.springframework.cloud.client.circuitbreaker.Customizer;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;

import java.time.Duration;
import java.time.LocalDateTime;

@SpringBootApplication
public class GatewayserverApplication {

	public static void main(String[] args) {
		SpringApplication.run(GatewayserverApplication.class, args);
	}

	@Bean
	public RouteLocator eazybankRouteConfig(RouteLocatorBuilder builder){
		return builder.routes()
				.route(p -> p
						.path("/eazybank/accounts/**")
						.filters(f -> f
								.rewritePath("/eazybank/accounts/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-ResponseTime", LocalDateTime.now().toString())
								.addRequestHeader("X-Gateway", "EazyBank-GW")
								.addRequestParameter("source", "gateway")
								.circuitBreaker(c -> c
										.setName("accountsCircuitBreaker")
										.setFallbackUri("forward:/contact-support")
								)
//								.prefixPath("/api")  // adds /api before forwarding
						)
						.uri("lb://ACCOUNTS")
				)
				.route(p -> p
						.path("/eazybank/cards/**")
						.filters(f -> f
								.rewritePath("/eazybank/cards/(?<segment>.*)", "/${segment}")
								.addResponseHeader("X-ResponseTime", LocalDateTime.now().toString())
//								.retry(config -> config.setRetries(3)) // retry failed requests
						)
						.uri("lb://CARDS")
				)
				.route(p -> p
						.path("/eazybank/loans/**")
						.filters(f -> f
								.rewritePath("/eazybank/loans/(?<segment>.*)", "/${segment}")
								.circuitBreaker(c -> c
										.setName("loanCircuitBreaker")
										).retry(retryConfig -> retryConfig.setRetries(3)
                                        .setMethods(HttpMethod.GET)
										.setBackoff(Duration.ofMillis(100),Duration.ofMillis(1000),2,true))
						)
						.uri("lb://LOANS")
				)
				.build();
	}
    @Bean
    public Customizer<Resilience4JCircuitBreakerFactory> defaultCustomizer() {
        return factory -> factory.configureDefault(id -> new Resilience4JConfigBuilder(id)
                .circuitBreakerConfig(CircuitBreakerConfig.ofDefaults())
                .timeLimiterConfig(TimeLimiterConfig.custom().timeoutDuration(Duration.ofSeconds(4)).build())
                .build());
    }



}
