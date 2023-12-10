package com.gmail.shablinski93.service.exception;

public class DataReaderException extends RuntimeException {
    public DataReaderException() {
        System.out.println("Ups! Some problem with text reading in input!");
    }
}