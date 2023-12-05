package com.gmail.shablinski93.service;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public interface DishService {
    Dish createDish() throws IOException, SQLException;

    Ingredient createIngredient() throws IOException;

    List<String> getAllDishesNames() throws SQLException;

    Dish findDishByName(String dishName) throws SQLException;

    List<Dish> sortDishByCalories() throws SQLException;

    List<Dish> sortDishByName() throws SQLException;

    void saveInFile() throws IOException, SQLException;
}