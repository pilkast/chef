package com.gmail.shablinski93;

import com.gmail.shablinski93.service.TaskService;
import com.gmail.shablinski93.service.impl.TaskServiceImpl;

import java.io.IOException;
import java.sql.SQLException;

public class App {
    public static void main(String[] args) throws IOException, SQLException {
        TaskService taskService = new TaskServiceImpl();
        taskService.runTask();
    }
}