package com.gmail.shablinski93.service.impl;

import com.gmail.shablinski93.repository.ReadFileRepository;
import com.gmail.shablinski93.repository.impl.ReadFileRepositoryImpl;
import com.gmail.shablinski93.service.DishService;
import com.gmail.shablinski93.service.TaskApp;
import com.gmail.shablinski93.service.exception.DataReaderException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TaskAppImpl implements TaskApp {
    private static final String FILE_PATH = "src/BaseInitialization.txt";
    private final DishService dishService = new DishServiceImpl();
    private final ReadFileRepository readFileRepository = new ReadFileRepositoryImpl();

    @Override
    public void runTask() {
        readFileRepository.getListStringsOfOriginalFile(FILE_PATH);
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            while (true) {
                System.out.println("Выберите нужное из списка:");
                System.out.println("1.Показать список блюд");
                System.out.println("2.Добавить блюдо");
                System.out.println("3.Отсортировать по кол-ву колорий");
                System.out.println("4.Отсортировать по названию блюда");
                System.out.println("5.Сохранить данные файл");
                System.out.println("6.Закончить работу приложения");
                String num = reader.readLine();

                switch (num) {
                    case "1":
                        List<String> dishes = dishService.getAllDishesNames();
                        dishes.forEach(System.out::println);
                        System.out.println("Введите наименование блюда для просмотра рецепта: ");
                        String getDishName = reader.readLine();
                        System.out.println(dishService.findDishByName(getDishName));
                        break;
                    case "2":
                        dishService.createDish();
                        break;
                    case "3":
                        System.out.println(dishService.sortDishByCalories());
                        break;
                    case "4":
                        System.out.println(dishService.sortDishByName());
                        break;
                    case "5":
                        dishService.saveInFile();
                        break;
                    case "6":
                        break;
                }
            }
        } catch (IOException e) {
            throw new DataReaderException();
        }
    }
}