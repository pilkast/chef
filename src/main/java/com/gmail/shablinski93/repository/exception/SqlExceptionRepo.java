package com.gmail.shablinski93.repository.exception;

public class SqlExceptionRepo extends RuntimeException {
    public SqlExceptionRepo() {
        System.out.println("Ups! Some problem with database!");
    }
}