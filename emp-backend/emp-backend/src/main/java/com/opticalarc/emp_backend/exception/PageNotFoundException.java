package com.opticalarc.emp_backend.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.BAD_REQUEST)
public class PageNotFoundException extends RuntimeException{
    public PageNotFoundException (String message){
        super(message);
    }
}
