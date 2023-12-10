package com.gmail.shablinski93.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Ingredient implements Serializable {
    private UUID ingredientId;
    private String ingredientName;
    private Integer ingredientCalories;
    List<Dish> dishes = new ArrayList<>();

    public UUID getIngredientId() {
        return ingredientId;
    }

    public void setIngredientId(UUID ingredientId) {
        this.ingredientId = ingredientId;
    }

    public String getIngredientName() {
        return ingredientName;
    }

    public void setIngredientName(String ingredientName) {
        this.ingredientName = ingredientName;
    }

    public Integer getIngredientCalories() {
        return ingredientCalories;
    }

    public void setIngredientCalories(Integer ingredientCalories) {
        this.ingredientCalories = ingredientCalories;
    }

    public List<Dish> getDishes() {
        return dishes;
    }

    public void setDishes(List<Dish> dishes) {
        this.dishes = dishes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return Objects.equals(ingredientId, that.ingredientId) && Objects.equals(ingredientName, that.ingredientName) && Objects.equals(ingredientCalories, that.ingredientCalories) && Objects.equals(dishes, that.dishes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(ingredientId, ingredientName, ingredientCalories, dishes);
    }

    @Override
    public String toString() {
        return "Ingredient:" +
                "ingredientName='" + ingredientName + '\'' +
                ", ingredientCalories=" + ingredientCalories;
    }
}