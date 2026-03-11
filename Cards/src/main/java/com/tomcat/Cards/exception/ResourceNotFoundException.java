package com.tomcat.Cards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public ResourceNotFoundException(String card, String cardNumber, String cardNumber1) {
        super(String.format("none any %s found with %s : %s", card, cardNumber, cardNumber1));
    }
}
