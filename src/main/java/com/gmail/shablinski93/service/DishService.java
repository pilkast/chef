package com.gmail.shablinski93.service;

import com.gmail.shablinski93.Dish;
import com.gmail.shablinski93.Ingredient;

import java.io.IOException;
import java.util.List;

public interface DishService {
    Dish createDish() throws IOException;

    Ingredient createIngredient() throws IOException;

    List<Dish> getAllDish();

    Dish findDishByName(String dishName);

    void saveInFile();
}

