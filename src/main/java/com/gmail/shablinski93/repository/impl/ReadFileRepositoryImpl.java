package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.repository.ConnectionRepository;
import com.gmail.shablinski93.repository.ReadFileRepository;
import com.gmail.shablinski93.service.DataIterationService;
import com.gmail.shablinski93.service.impl.DataIterationServiceImpl;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadFileRepositoryImpl implements ReadFileRepository {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository = ConnectionRepositoryImpl.getInstance();
    private final DataIterationService dis = new DataIterationServiceImpl();

    @Override
    public void readCommandFromFile(String nameFile) {
        List<String> listStrings = new ArrayList<>();
        String line = dis.lineReader();
        while (line != null) {
            listStrings.add(line);
            line = dis.lineReader();
        }
        listStrings.forEach(this::executeCommand);
    }

    private void executeCommand(String item) {
        try {
            Connection connection = connectionRepository.getConnection();
            connection.setAutoCommit(true);
            PreparedStatement statement = connection.prepareStatement(item);
            statement.execute();
        } catch (SQLException e) {
            logger.error("Error when reading file.", e.getMessage(), e);
        }
    }
}