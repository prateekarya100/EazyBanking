package com.tomcat.Cards.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data @AllArgsConstructor  @NoArgsConstructor
public class ErrorResponseDto {
    private String apiPath;
    private HttpStatus httpStatus;
    private String message;
    private LocalDateTime timestamp;
}
