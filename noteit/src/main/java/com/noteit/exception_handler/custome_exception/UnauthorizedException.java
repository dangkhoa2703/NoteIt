package com.noteit.exception_handler.custome_exception;

import org.springframework.web.client.HttpClientErrorException;

public class UnauthorizedException extends Exception {

    public UnauthorizedException(String message) {
        super(message);
    }
}
