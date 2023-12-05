package com.gmail.shablinski93.repository;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.UUID;

public interface DishRepository {
    Dish addDish(Connection connection, Dish dish) throws SQLException;

    void addIngredientList(Connection connection, Dish dish) throws SQLException;

    List<UUID> getIngredientsIdForDish(Connection connection, String dishId) throws SQLException;

    List<Ingredient> getIngredientsByIdForDish(Connection connection, List<UUID> ingredientsIdForDish) throws SQLException;

    List<Dish> getAllDish(Connection connection) throws SQLException;

    Dish getDishByName(Connection connection, String dishName) throws SQLException;
}