package com.tomcatdevs.Accounts.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resource, String type, String value) {
        super(String.format("sorry, no %s is mapped with %s : %s",resource,type,value));
    }
}
