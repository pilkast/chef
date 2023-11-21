package com.gmail.shablinski93.service;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;

import java.io.IOException;
import java.util.List;

public interface DishService {
    Dish createDish() throws IOException;

    Ingredient createIngredient() throws IOException;

    List<Dish> getAllDish();

    Dish findDishByName(String dishName);

    List<Dish> sortDishByCalories();

    public List<Dish> sortDishByIngredients();

    List<Dish> sortDishByName();

    void saveInFile();
}

