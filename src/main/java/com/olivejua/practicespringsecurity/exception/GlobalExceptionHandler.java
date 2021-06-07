package com.olivejua.practicespringsecurity.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(AccessDeniedException.class)
    protected ResponseEntity handleAccessDeniedException(AccessDeniedException e) {
        logger.error(e.getMessage());
        return new ResponseEntity(HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(NotExistingUserException.class)
    protected ResponseEntity handleNotExistingUserException(NotExistingUserException e ) {
        logger.error(e.getMessage());
        return new ResponseEntity(HttpStatus.UNAUTHORIZED);
    }
}
