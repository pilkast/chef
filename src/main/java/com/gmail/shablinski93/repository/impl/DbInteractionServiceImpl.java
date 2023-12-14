package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.repository.DbInteractionService;
import com.gmail.shablinski93.repository.exception.SqlExceptionRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DbInteractionServiceImpl implements DbInteractionService {
    @Override
    public ResultSet executeQueryOneParam(Connection connection, String sqlQuery, Integer paramIndex, String paramData) {
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(paramIndex, paramData);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new SqlExceptionRepo(e);
        }
        return resultSet;
    }

    @Override
    public ResultSet executeQueryNoParam(Connection connection, String sqlQuery) {
        ResultSet resultSet;
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            throw new SqlExceptionRepo(e);
        }
        return resultSet;
    }
}