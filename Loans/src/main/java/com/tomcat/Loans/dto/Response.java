package com.tomcat.Loans.dto;

import lombok.*;
import org.springframework.http.HttpStatus;

@Setter @Getter @ToString @AllArgsConstructor @NoArgsConstructor
public class Response {
    private HttpStatus status;
    private String response;
}
