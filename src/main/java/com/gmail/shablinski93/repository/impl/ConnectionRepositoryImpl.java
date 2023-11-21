package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.repository.ConnectionRepository;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;

public class ConnectionRepositoryImpl implements ConnectionRepository {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final String DATASOURCE_URL = "jdbc:postgresql://localhost:5432/food";
    private static final String DATASOURCE_USERNAME = "user";
    private static final String DATASOURCE_PASSWORD = "qwerty";
    private static ConnectionRepository instance;

    private HikariDataSource dataSource;

    private ConnectionRepositoryImpl() {
    }

    public static ConnectionRepository getInstance() {
        if (instance == null) {
            instance = new ConnectionRepositoryImpl();
        }
        return instance;
    }

    @Override
    public Connection getConnection() {
        if (dataSource == null) {
            HikariConfig hikariConfig = new HikariConfig();
            hikariConfig.setJdbcUrl(DATASOURCE_URL);
            hikariConfig.setUsername(DATASOURCE_USERNAME);
            hikariConfig.setPassword(DATASOURCE_PASSWORD);

            this.dataSource = new HikariDataSource(hikariConfig);
        }
        try {
            Connection connection = this.dataSource.getConnection();
            connection.setAutoCommit(false);
            return connection;
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new IllegalStateException(e);
        }
    }
}
