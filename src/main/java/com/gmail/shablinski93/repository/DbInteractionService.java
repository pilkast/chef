package com.gmail.shablinski93.repository;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public interface DbInteractionService {
    ResultSet executeQueryOneParam(String sqlQuery, Integer paramIndex, String paramData);

    ResultSet executeQueryNoParam(String sqlQuery);

    boolean executeUpdateDish(String insertQueryEntity, HashMap<Integer, String> mapEntity);

    void executeUpdateIngredient(String insertQueryIngredient, List<Ingredient> entityList);

    void executeUpdateDependency(String insertQueryDependency, HashMap<Integer, List<String>> dependencyValue);

    List<UUID> getIngredientsId(ResultSet resultSet);

    List<Dish> resultSetToList(ResultSet resultSet);

    Ingredient oneIngredient(ResultSet resultSet);
}