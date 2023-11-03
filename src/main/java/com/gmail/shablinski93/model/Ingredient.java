package com.gmail.shablinski93.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "ingredients")
public class Ingredient implements Serializable {
    @Id
    private UUID ingredientId;
    private String ingredientName;
    private Integer ingredientCalories;

    @ManyToMany(mappedBy = "ingredients")
    List<Dish> dishes = new ArrayList<Dish>();
}
