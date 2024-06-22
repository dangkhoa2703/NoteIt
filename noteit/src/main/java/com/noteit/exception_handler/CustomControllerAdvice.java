package com.noteit.exception_handler;

import com.noteit.exception_handler.custome_exception.UnauthorizedException;
import com.noteit.exception_handler.custome_exception.UserNameAlreadyExistsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;


@ControllerAdvice
public class CustomControllerAdvice {

    @ExceptionHandler({BadCredentialsException.class, UnauthorizedException.class})
    public ResponseEntity<ErrorResponse> handleAuthenticationExceptions(Exception e) {
        HttpStatus status = HttpStatus.UNAUTHORIZED;
        ErrorResponse errorResponse = ErrorResponse.create(e,status,e.getMessage());

        return new ResponseEntity<>(errorResponse,status);
    }

    @ExceptionHandler(UserNameAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleRegistrationException(Exception e){
        HttpStatus status = HttpStatus.CONFLICT;
        ErrorResponse errorResponse = ErrorResponse.create(e,status,e.getMessage());

        return new ResponseEntity<>( errorResponse, status);
    }



}
