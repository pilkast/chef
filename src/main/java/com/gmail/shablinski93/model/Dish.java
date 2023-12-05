package com.gmail.shablinski93.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

public class Dish implements Serializable {
    private UUID dishId;
    private String dishName;
    private Integer caloriesCount;
    private Integer ingredientCount;

    List<Ingredient> ingredients = new ArrayList<Ingredient>();

    public UUID getDishId() {
        return dishId;
    }

    public void setDishId(UUID dishId) {
        this.dishId = dishId;
    }

    public String getDishName() {
        return dishName;
    }

    public void setDishName(String dishName) {
        this.dishName = dishName;
    }

    public Integer getCaloriesCount() {
        return caloriesCount;
    }

    public void setCaloriesCount(Integer caloriesCount) {
        this.caloriesCount = caloriesCount;
    }

    public Integer getIngredientCount() {
        return ingredientCount;
    }

    public void setIngredientCount(Integer ingredientCount) {
        this.ingredientCount = ingredientCount;
    }

    public List<Ingredient> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dish dish = (Dish) o;
        return Objects.equals(dishId, dish.dishId) && Objects.equals(dishName, dish.dishName) && Objects.equals(caloriesCount, dish.caloriesCount) && Objects.equals(ingredientCount, dish.ingredientCount) && Objects.equals(ingredients, dish.ingredients);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dishId, dishName, caloriesCount, ingredientCount, ingredients);
    }

    @Override
    public String toString() {
        return "Dish:" + "dishName='" + dishName + '\'' + ", caloriesCount=" + caloriesCount + ", ingredientCount=" + ingredientCount + ", ingredients=" + ingredients.stream().map(Objects::toString).collect(Collectors.toList());
    }
}