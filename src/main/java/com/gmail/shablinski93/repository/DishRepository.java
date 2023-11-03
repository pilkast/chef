package com.gmail.shablinski93.repository;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.DishIngredient;
import com.gmail.shablinski93.model.Ingredient;

import java.sql.Connection;
import java.util.List;
import java.util.UUID;

public interface DishRepository {
    Dish addDish(Connection connection, Dish dish);

    List<Ingredient> addIngredientList(Connection connection, Dish dish);

    List<UUID> getIngredientsIdForDish(Connection connection, String dishId);

    List<Ingredient> getIngredientsByIdForDish(Connection connection, List<UUID> ingredientsIdForDish);

    DishIngredient creatingDependence(Connection connection, DishIngredient dishIngredient, Dish dish, Ingredient ingredient);

    List<Dish> getAllDish(Connection connection);

    Dish getDishByName(Connection connection, String dishName);
}
