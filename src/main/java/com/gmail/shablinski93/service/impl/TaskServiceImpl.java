package com.gmail.shablinski93.service.impl;

import com.gmail.shablinski93.service.DishService;
import com.gmail.shablinski93.service.TaskService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class TaskServiceImpl implements TaskService {
    private final DishService dishService = new DishServiceImpl();

    @Override
    public void runTask() throws IOException, SQLException {

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
    }
}