package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.DbInteractionService;
import com.gmail.shablinski93.repository.DishRepository;

import java.sql.ResultSet;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DishRepoImpl implements DishRepository {
    private static final String INSERT_QUERY_DISH = "INSERT INTO dish (dish_id, dishname, caloriescount, ingredientcount) VALUES (?,?,?,?)";
    private static final String INSERT_QUERY_INGREDIENT = "INSERT INTO ingredients (ingredient_id, ingredient_name, ingredient_calories) VALUES (?,?,?)";
    private static final String INSERT_QUERY_DEPENDENCY = "INSERT INTO dish_ingredient (main_dish_id, main_ingredient_id) VALUES (?,?)";
    private static final String SELECT_FROM_DISHES = "SELECT * FROM dish";
    private static final String SELECT_FROM_INGREDIENTS_ID_FOR_DISH = "SELECT main_ingredient_id FROM dish_ingredient WHERE main_dish_id = ?";
    private static final String SELECT_FROM_INGREDIENTS_BY_ID = "SELECT * FROM ingredients WHERE ingredient_id = ?";
    private static final String SELECT_QUERY_BY_DISH_NAME = "SELECT * FROM dish WHERE dishname = ?";

    private final DbInteractionService dbIs = new DbInteractionServiceImpl();

    @Override
    public Dish addDish(Dish dish) {
        HashMap<Integer, String> dishValue = new HashMap<>();
        dishValue.put(1, dish.getDishId().toString());
        dishValue.put(2, dish.getDishName());
        dishValue.put(3, dish.getCaloriesCount().toString());
        dishValue.put(4, dish.getCaloriesCount().toString());

        dbIs.executeUpdateDish(INSERT_QUERY_DISH, dishValue);

        addIngredientList(dish);
        addDishIdAndIngredientId(dish);
        return dish;
    }

    @Override
    public void addIngredientList(Dish dish) {
        List<Ingredient> ingredients = dish.getIngredients();
        dbIs.executeUpdateIngredient(INSERT_QUERY_INGREDIENT, ingredients);
    }

    @Override
    public List<UUID> getIngredientsIdForDish(String dishId) {
        ResultSet resultSet = dbIs.executeQueryOneParam(SELECT_FROM_INGREDIENTS_ID_FOR_DISH, 1, dishId);
        return dbIs.getIngredientsId(resultSet);
    }

    @Override
    public List<Ingredient> getIngredientsByIdForDish(List<UUID> ingredientsIdForDish) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (UUID idForDish : ingredientsIdForDish) {
            ResultSet resultSet = dbIs.executeQueryOneParam(SELECT_FROM_INGREDIENTS_BY_ID, 1, idForDish.toString());
            ingredients.add(dbIs.oneIngredient(resultSet));
        }
        return ingredients;
    }

    @Override
    public List<Dish> getAllDish() {
        ResultSet resultSet = dbIs.executeQueryNoParam(SELECT_FROM_DISHES);
        return dbIs.resultSetToList(resultSet);
    }

    @Override
    public Dish getDishByName(String dishName) {
        ResultSet resultSet = dbIs.executeQueryOneParam(SELECT_QUERY_BY_DISH_NAME, 1, dishName);
        Optional<Dish> optional = dbIs.resultSetToList(resultSet).stream().findFirst();
        Dish dish = new Dish();
        return optional.orElse(dish);
    }

    private void addDishIdAndIngredientId(Dish dish) {
        List<Ingredient> ingredients = dish.getIngredients();
        List<String> idList = ingredients
                .stream()
                .map(Ingredient::getIngredientId)
                .map(UUID::toString)
                .collect(Collectors.toList());
        List<String> collectDishId = Stream.of(dish.getDishId().toString())
                .collect(Collectors.toList());
        HashMap<Integer, List<String>> dependencyValue = new HashMap<>();
        dependencyValue.put(1, collectDishId);
        dependencyValue.put(2, idList);

        dbIs.executeUpdateDependency(INSERT_QUERY_DEPENDENCY, dependencyValue);
    }
}