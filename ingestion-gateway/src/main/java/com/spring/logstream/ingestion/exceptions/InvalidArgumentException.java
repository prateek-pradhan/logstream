package com.spring.logstream.ingestion.exceptions;

public class InvalidArgumentException extends IllegalArgumentException {

    public InvalidArgumentException(String message) {
        super(message);
    }
}
