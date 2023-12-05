package com.gmail.shablinski93.service;

import java.io.IOException;
import java.sql.SQLException;

public interface TaskService {
    void runTask() throws IOException, SQLException;
}