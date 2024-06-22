package com.noteit.exception_handler.custome_exception;

import javax.naming.AuthenticationException;

public class UserNameAlreadyExistsException extends AuthenticationException {

    public UserNameAlreadyExistsException(String explanation) {
        super(explanation);
    }
}
