package com.meesho.dmp.common.exceptions;

public class CsvReadingException extends RuntimeException {
    public CsvReadingException(String message, Throwable cause) {
        super(message, cause);
    }
}
