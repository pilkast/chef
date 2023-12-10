package com.gmail.shablinski93.service;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;

import java.util.List;

public interface DishService {
    Dish createDish();

    Ingredient createIngredient();

    List<String> getAllDishesNames();

    Dish findDishByName(String dishName);

    List<Dish> sortDishByCalories();

    List<Dish> sortDishByName();

    void saveInFile();
}