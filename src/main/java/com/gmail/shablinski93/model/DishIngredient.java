package com.gmail.shablinski93.model;

import lombok.Data;

import java.util.UUID;

@Data
public class DishIngredient {
    private UUID mainDishId;
    private UUID mainIngredientId;
}