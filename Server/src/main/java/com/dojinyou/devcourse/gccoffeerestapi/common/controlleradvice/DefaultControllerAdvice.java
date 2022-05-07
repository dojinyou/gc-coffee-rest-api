package com.dojinyou.devcourse.gccoffeerestapi.common.controlleradvice;

import com.dojinyou.devcourse.gccoffeerestapi.common.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class DefaultControllerAdvice {

    @ExceptionHandler
    public ResponseEntity notFoundExceptionHandler(NotFoundException e) {
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler
    public ResponseEntity IllegalArgumentExceptionHandler(IllegalArgumentException e) {
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
}
