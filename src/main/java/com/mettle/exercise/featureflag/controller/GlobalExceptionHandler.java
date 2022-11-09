package com.mettle.exercise.featureflag.controller;

import com.mettle.exercise.featureflag.exception.DuplicatedFeatureException;
import com.mettle.exercise.featureflag.exception.FeatureNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(DuplicatedFeatureException.class)
    @ResponseStatus(HttpStatus.CONFLICT)
    public void handleDuplicatedFeatureException() {}

    @ExceptionHandler(FeatureNotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public void handleFeatureNotFoundException() {}
}
