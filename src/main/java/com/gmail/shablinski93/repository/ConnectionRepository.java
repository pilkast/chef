package com.gmail.shablinski93.repository;

import java.sql.Connection;

public interface ConnectionRepository {
    Connection getConnection();
}