package com.gmail.shablinski93.repository;

import java.sql.Connection;
import java.sql.ResultSet;

public interface DbInteractionService {
    ResultSet executeQueryOneParam(Connection connection, String sqlQuery, Integer paramIndex, String paramData);

    ResultSet executeQueryNoParam(Connection connection, String sqlQuery);
}