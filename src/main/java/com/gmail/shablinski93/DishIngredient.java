package com.gmail.shablinski93;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Data
public class DishIngredient {
    private UUID mainDishId;
    private UUID mainIngredientId;
}
