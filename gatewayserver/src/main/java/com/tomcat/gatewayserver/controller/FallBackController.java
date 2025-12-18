package com.tomcat.gatewayserver.controller;

import com.tomcat.gatewayserver.dto.ResponseContactSupportTeam;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallBackController {

    // fallback for contact-support of accounts
    @GetMapping("/contact-support")
    public Mono<ResponseEntity<ResponseContactSupportTeam>> accountsContactSupportFallback() {
        return Mono.just(ResponseEntity.ok(new ResponseContactSupportTeam("Accounts Service is down. Please try again later. or contact support team at support@eazybank.com")));
    }
}
