package com.gmail.shablinski93.repository.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

public class SqlExceptionRepo extends RuntimeException {

    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    public SqlExceptionRepo(Exception e) {
        logger.error("Some problem with SQL request", e.getMessage(), e);
    }
}