package com.gmail.shablinski93.service.exception;

public class ConnectionException extends RuntimeException {
    public ConnectionException() {
        System.out.println("Ups! Some problem with connection!");
    }
}