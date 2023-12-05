package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.DishRepository;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DishRepoImpl implements DishRepository {
    private static final String INSERT_QUERY_DISH = "INSERT INTO dish (dish_id, dishname, caloriescount, ingredientcount) VALUES (?,?,?,?)";
    private static final String INSERT_QUERY_INGREDIENT = "INSERT INTO ingredients (ingredient_id, ingredient_name, ingredient_calories) VALUES (?,?,?)";
    private static final String INSERT_QUERY_DEPENDENCY = "INSERT INTO dish_ingredient (main_dish_id, main_ingredient_id) VALUES (?,?)";
    private static final String SELECT_FROM_DISHES = "SELECT * FROM dish";
    private static final String SELECT_FROM_INGREDIENTS_ID_FOR_DISH = "SELECT main_ingredient_id FROM dish_ingredient WHERE main_dish_id = ?";
    private static final String SELECT_FROM_INGREDIENTS_BY_ID = "SELECT * FROM ingredients WHERE ingredient_id = ?";
    private static final String SELECT_QUERRY_BY_DISH_NAME = "SELECT * FROM dish WHERE dishname = ?";

    @Override
    public Dish addDish(Connection connection, Dish dish) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement
                (INSERT_QUERY_DISH);

        preparedStatement.setString(1, dish.getDishId().toString());
        preparedStatement.setString(2, dish.getDishName());
        preparedStatement.setInt(3, dish.getCaloriesCount());
        preparedStatement.setInt(4, dish.getIngredientCount());
        preparedStatement.executeUpdate();

        addIngredientList(connection, dish);
        addDishIdAndIngredientId(connection, dish);
        return dish;
    }

    private void addDishIdAndIngredientId(Connection connection, Dish dish) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_DEPENDENCY);

        List<Ingredient> ingredients = dish.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            preparedStatement.setString(1, dish.getDishId().toString());
            preparedStatement.setString(2, ingredient.getIngredientId().toString());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public void addIngredientList(Connection connection, Dish dish) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_INGREDIENT);
        List<Ingredient> ingredients = dish.getIngredients();
        for (Ingredient ingred : ingredients) {
            preparedStatement.setString(1, ingred.getIngredientId().toString());
            preparedStatement.setString(2, ingred.getIngredientName());
            preparedStatement.setInt(3, ingred.getIngredientCalories());
            preparedStatement.executeUpdate();
        }
    }

    @Override
    public List<UUID> getIngredientsIdForDish(Connection connection, String dishId) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_INGREDIENTS_ID_FOR_DISH);
        preparedStatement.setString(1, dishId);
        List<UUID> list = new ArrayList<>();
        ResultSet resultSet = preparedStatement.executeQuery();
        while (resultSet.next()) {
            String item = resultSet.getString("main_ingredient_id");
            list.add(UUID.fromString(item));
        }
        return list;
    }

    @Override
    public List<Ingredient> getIngredientsByIdForDish(Connection connection, List<UUID> ingredientsIdForDish) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_INGREDIENTS_BY_ID);
        List<Ingredient> ingredients = new ArrayList<>();
        for (UUID idForDish : ingredientsIdForDish) {
            preparedStatement.setString(1, idForDish.toString());
            ingredients.add(oneIngredient(preparedStatement.executeQuery()));
        }
        return ingredients;
    }

    @Override
    public List<Dish> getAllDish(Connection connection) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_FROM_DISHES);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSetToList(resultSet);
    }

    @Override
    public Dish getDishByName(Connection connection, String dishName) throws SQLException {
        PreparedStatement preparedStatement = connection.prepareStatement(SELECT_QUERRY_BY_DISH_NAME);
        preparedStatement.setString(1, dishName);
        ResultSet resultSet = preparedStatement.executeQuery();
        return resultSetToList(resultSet).stream().findFirst().get();
    }

    private List<Dish> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        while (resultSet.next()) {
            Dish dish = new Dish();
            dish.setDishId(UUID.fromString(resultSet.getString("dish_id")));
            dish.setDishName(resultSet.getString("dishname"));
            dish.setCaloriesCount(resultSet.getInt("caloriescount"));
            dish.setIngredientCount(resultSet.getInt("ingredientcount"));

            dishes.add(dish);
        }
        return dishes;
    }

    private Ingredient oneIngredient(ResultSet resultSet) throws SQLException {
        Ingredient ingredient = new Ingredient();
        while (resultSet.next()) {
            ingredient.setIngredientId(UUID.fromString(resultSet.getString("ingredient_id")));
            ingredient.setIngredientName(resultSet.getString("ingredient_name"));
            ingredient.setIngredientCalories(resultSet.getInt("ingredient_calories"));
        }
        return ingredient;
    }
}