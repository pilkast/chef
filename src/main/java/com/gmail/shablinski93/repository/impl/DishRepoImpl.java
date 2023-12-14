package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.DbInteractionService;
import com.gmail.shablinski93.repository.DishRepository;
import com.gmail.shablinski93.repository.exception.SqlExceptionRepo;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

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
    public Dish addDish(Connection connection, Dish dish) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement
                    (INSERT_QUERY_DISH);
            preparedStatement.setString(1, dish.getDishId().toString());
            preparedStatement.setString(2, dish.getDishName());
            preparedStatement.setInt(3, dish.getCaloriesCount());
            preparedStatement.setInt(4, dish.getIngredientCount());
            preparedStatement.executeUpdate();
            addIngredientList(connection, dish);
            addDishIdAndIngredientId(connection, dish);
        } catch (SQLException e) {
            throw new SqlExceptionRepo(e);
        }
        return dish;
    }

    private void addDishIdAndIngredientId(Connection connection, Dish dish) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_DEPENDENCY);
            List<Ingredient> ingredients = dish.getIngredients();
            for (int i = 0; i < ingredients.size(); i++) {
                Ingredient ingredient = ingredients.get(i);
                preparedStatement.setString(1, dish.getDishId().toString());
                preparedStatement.setString(2, ingredient.getIngredientId().toString());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SqlExceptionRepo(e);
        }
    }

    @Override
    public void addIngredientList(Connection connection, Dish dish) {
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(INSERT_QUERY_INGREDIENT);
            List<Ingredient> ingredients = dish.getIngredients();
            for (Ingredient ingred : ingredients) {
                preparedStatement.setString(1, ingred.getIngredientId().toString());
                preparedStatement.setString(2, ingred.getIngredientName());
                preparedStatement.setInt(3, ingred.getIngredientCalories());
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            throw new SqlExceptionRepo(e);
        }
    }

    @Override
    public List<UUID> getIngredientsIdForDish(Connection connection, String dishId) {
        List<UUID> list = new ArrayList<>();
        ResultSet resultSet = dbIs.executeQueryOneParam(connection, SELECT_FROM_INGREDIENTS_ID_FOR_DISH, 1, dishId);
        while (true) {
            try {
                if (!resultSet.next()) break;
                String item = resultSet.getString("main_ingredient_id");
                list.add(UUID.fromString(item));
            } catch (SQLException e) {
                throw new SqlExceptionRepo(e);
            }
        }
        return list;
    }

    @Override
    public List<Ingredient> getIngredientsByIdForDish(Connection connection, List<UUID> ingredientsIdForDish) {
        List<Ingredient> ingredients = new ArrayList<>();
        for (UUID idForDish : ingredientsIdForDish) {
            ResultSet resultSet = dbIs.executeQueryOneParam(connection, SELECT_FROM_INGREDIENTS_BY_ID, 1, idForDish.toString());
            ingredients.add(oneIngredient(resultSet));
        }
        return ingredients;
    }

    @Override
    public List<Dish> getAllDish(Connection connection) {
        ResultSet resultSet = dbIs.executeQueryNoParam(connection, SELECT_FROM_DISHES);
        return resultSetToList(resultSet);
    }

    @Override
    public Dish getDishByName(Connection connection, String dishName) {
        ResultSet resultSet = dbIs.executeQueryOneParam(connection, SELECT_QUERY_BY_DISH_NAME, 1, dishName);
        Optional<Dish> optional = resultSetToList(resultSet).stream().findFirst();
        Dish dish = new Dish();
        if (optional.isPresent()) {
            dish = optional.get();
        }
        return dish;
    }

    private List<Dish> resultSetToList(ResultSet resultSet) {
        List<Dish> dishes = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next())
                    break;
                Dish dish = new Dish();
                dish.setDishId(UUID.fromString(resultSet.getString("dish_id")));
                dish.setDishName(resultSet.getString("dishname"));
                dish.setCaloriesCount(resultSet.getInt("caloriescount"));
                dish.setIngredientCount(resultSet.getInt("ingredientcount"));
                dishes.add(dish);
            } catch (SQLException e) {
                throw new SqlExceptionRepo(e);
            }
        }
        return dishes;
    }

    private Ingredient oneIngredient(ResultSet resultSet) {
        Ingredient ingredient = new Ingredient();
        while (true) {
            try {
                if (!resultSet.next())
                    break;
                ingredient.setIngredientId(UUID.fromString(resultSet.getString("ingredient_id")));
                ingredient.setIngredientName(resultSet.getString("ingredient_name"));
                ingredient.setIngredientCalories(resultSet.getInt("ingredient_calories"));
            } catch (SQLException e) {
                throw new SqlExceptionRepo(e);
            }
        }
        return ingredient;
    }
}