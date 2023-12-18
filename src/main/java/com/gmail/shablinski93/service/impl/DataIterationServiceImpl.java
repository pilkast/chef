package com.gmail.shablinski93.service.impl;

import com.gmail.shablinski93.service.DataIterationService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.util.List;

public class DataIterationServiceImpl implements DataIterationService {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());

    @Override
    public String lineReader() {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String readline = null;
        try {
            readline = reader.readLine();
        } catch (IOException e) {
            logger.error("Error with input parse.", e.getMessage(), e);
        }
        return readline;
    }

    @Override
    public void createFileWithInfoFromDb(String fileNameAndPath) {
        File file = new File(fileNameAndPath);
        try {
            if (file.createNewFile()) {
                System.out.println("Файл создан");
            } else {
                System.out.println("Файл уже существует");
            }
        } catch (IOException e) {
            logger.error("Error with file creating.", e.getMessage(), e);
        }
    }

    @Override
    public void saveInFile(String fileName, List<String> someInfo) {
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(fileName);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(someInfo);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            logger.error("Some error with save in file.", e.getMessage(), e);
        }
    }
}
