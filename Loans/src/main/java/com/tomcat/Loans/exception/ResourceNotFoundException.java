package com.tomcat.Loans.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String service, String type, String mobileNumber) {
        super(String.format("sorry, no %s found with %s : %s", service ,type,mobileNumber));
    }
}
