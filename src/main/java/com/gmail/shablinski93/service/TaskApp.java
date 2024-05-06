package com.gmail.shablinski93.service;

import com.gmail.shablinski93.repository.ReadFileRepository;
import com.gmail.shablinski93.repository.impl.ReadFileRepositoryImpl;
import com.gmail.shablinski93.service.impl.DataIterationServiceImpl;
import com.gmail.shablinski93.service.impl.DishServiceImpl;

import java.util.List;

public class TaskApp {
    private static final String FILE_PATH = "src/BaseInitialization.txt";
    private final DishService dishService = new DishServiceImpl();
    private final ReadFileRepository readFileRepository = new ReadFileRepositoryImpl();
    private final DataIterationService dataIterationService = new DataIterationServiceImpl();

    public void runTask() {
        readFileRepository.readCommandFromFile(FILE_PATH);
        while (true) {
            System.out.println("Выберите нужное из списка:");
            System.out.println("1.Показать список блюд");
            System.out.println("2.Добавить блюдо");
            System.out.println("3.Отсортировать по кол-ву колорий");
            System.out.println("4.Отсортировать по названию блюда");
            System.out.println("5.Сохранить данные файл");
            System.out.println("6.Закончить работу приложения");
            String num = dataIterationService.lineReader();
            switch (num) {
                case "1":
                    List<String> dishes = dishService.getAllDishesNames();
                    dishes.forEach(System.out::println);
                    System.out.println("Введите наименование блюда для просмотра рецепта: ");
                    String getDishName = dataIterationService.lineReader();
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
    }
}