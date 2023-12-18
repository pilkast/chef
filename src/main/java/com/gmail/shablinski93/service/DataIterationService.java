package com.gmail.shablinski93.service;

import java.util.List;

public interface DataIterationService {
    String lineReader();

    void createFileWithInfoFromDb(String fileNameAndPath);

    void saveInFile(String fileName, List<String> someInfo);
}
