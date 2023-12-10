package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.repository.ConnectionRepository;
import com.gmail.shablinski93.repository.ReadFileRepository;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ReadFileRepositoryImpl implements ReadFileRepository {
    private final ConnectionRepository connectionRepository = ConnectionRepositoryImpl.getInstance();

    @Override
    public void getListStringsOfOriginalFile(String nameFile) {
        List<String> listStrings = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader((new FileReader(nameFile)))) {
            String line = reader.readLine();
            while (line != null) {
                listStrings.add(line);
                line = reader.readLine();
            }
        } catch (IOException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
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
            throw new RuntimeException(e);
        }

    }
}