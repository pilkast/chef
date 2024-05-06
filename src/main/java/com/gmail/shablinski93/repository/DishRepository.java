package com.gmail.shablinski93.repository;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;

import java.util.List;
import java.util.UUID;

public interface DishRepository {
    Dish addDish(Dish dish);

    void addIngredientList(Dish dish);

    List<UUID> getIngredientsIdForDish(String dishId);

    List<Ingredient> getIngredientsByIdForDish(List<UUID> ingredientsIdForDish);

    List<Dish> getAllDish();

    Dish getDishByName(String dishName);
}