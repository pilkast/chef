package com.gmail.shablinski93;

import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "dish")
public class Dish implements Serializable {

    @Id
    private UUID dishId;
    private String dishName;
    private Integer caloriesCount;      //поменять на инте
    private Integer ingredientCount;     // тзмгнтьб
    @ManyToMany
    @JoinTable(name = "dish_ingredient",
            joinColumns = @JoinColumn(name = "mainDishId"),
            inverseJoinColumns = @JoinColumn(name = "mainIngredientId")
    )
    List<Ingredient> ingredients = new ArrayList<Ingredient>();

    @Override
    public String toString() {
        return "Dish{" +
                "dishName='" + dishName + '\'' +
                ", caloriesCount=" + caloriesCount +
                ", ingredientCount=" + ingredientCount +
                ", ingredients=" + ingredients +
                '}';
    }
}
