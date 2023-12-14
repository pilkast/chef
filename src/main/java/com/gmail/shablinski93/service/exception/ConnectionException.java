package com.gmail.shablinski93.service.exception;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;

public class ConnectionException extends RuntimeException {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    public ConnectionException(Exception e) {
        logger.error("Some problem with connection!", e.getMessage(), e);
    }
}