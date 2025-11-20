package com.tomcat.Cards.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.ALREADY_REPORTED)
public class CreditCardAlreadyExists extends RuntimeException {

    public CreditCardAlreadyExists(String card, String mobile, String mobileNumber) {
        super(String.format(" %s is already issued to customer with %s number %s", card, mobile,mobileNumber));
    }
}
