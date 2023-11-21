package com.gmail.shablinski93;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.service.DishService;
import com.gmail.shablinski93.service.impl.DishServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class App {

    private static final DishService dishService = new DishServiceImpl();

    public static void main(String[] args) throws IOException {

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Выберите нужное из списка:");
            System.out.println("1.Показать список блюд");
            System.out.println("2.Добавить блюдо");
            System.out.println("3.Отсортировать по кол-ву колорий");
            System.out.println("4.Посмотреть блюдо по названию");
            System.out.println("5.Сохранить данные файл");
            System.out.println("6.Закончить работу приложения");
            String num = reader.readLine();
            switch (num) {
                case "1":
                    List<Dish> dishes = dishService.getAllDish();
                    System.out.println(dishes);
                    break;
                case "2":
                    dishService.createDish();
                    break;
                case "3":
                    System.out.println(dishService.sortDishByCalories());
                    break;
                case "4":
                    System.out.println("Введите наименование блюда: ");
                    String name = reader.readLine();
                    System.out.println(dishService.findDishByName(name));
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