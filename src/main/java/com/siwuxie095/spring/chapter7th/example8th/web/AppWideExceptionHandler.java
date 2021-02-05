package com.siwuxie095.spring.chapter7th.example8th.web;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author Jiajing Li
 * @date 2021-02-05 20:57:10
 */
@SuppressWarnings("all")
@ControllerAdvice
public class AppWideExceptionHandler {

    @ExceptionHandler(DuplicateSpittleException.class)
    public String handleNotFound() {
        return "error/duplicate";
    }

}
