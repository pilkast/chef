package com.gmail.shablinski93;

import com.gmail.shablinski93.service.TaskApp;
import com.gmail.shablinski93.service.impl.TaskAppImpl;

public class App {
    public static void main(String[] args) {
        TaskApp taskApp = new TaskAppImpl();
        taskApp.runTask();
    }
}