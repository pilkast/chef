package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.DishRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class DishRepoImpl implements DishRepository {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static final String INSERT_QUERY_DISH = "INSERT INTO dish (dish_id, dishname, caloriescount, ingredientcount) VALUES (?,?,?,?)";

    private static final String INSERT_QUERY_INGREDIENT = "INSERT INTO ingredients (ingredient_id, ingredient_name, ingredient_calories) VALUES (?,?,?)";

    private static final String INSERT_QUERY_DEPENDENCY = "INSERT INTO dish_ingredient (main_dish_id, main_ingredient_id) VALUES (?,?)";

    private static final String SELECT_FROM_DISHES = "SELECT * FROM dish";
    private static final String SELECT_FROM_INGREDIENTS_ID_FOR_DISH = "SELECT main_ingredient_id FROM dish_ingredient WHERE main_dish_id = ?";

    private static final String SELECT_FROM_INGREDIENTS_BY_ID = "SELECT * FROM ingredients WHERE ingredient_id = ?";
    private static final String SELECT_QUERRY_BY_DISH_NAME = "SELECT * FROM dish WHERE dishname = ?";
    private static DishRepository instance;

    private DishRepoImpl() {
    }

    public static DishRepository getInstance() {
        if (instance == null) {
            instance = new DishRepoImpl();
        }
        return instance;
    }

    @Override
    public Dish addDish(Connection connection, Dish dish) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (INSERT_QUERY_DISH)) {
            preparedStatement.setString(1, dish.getDishId().toString());
            preparedStatement.setString(2, dish.getDishName());
            preparedStatement.setInt(3, dish.getCaloriesCount());
            preparedStatement.setInt(4, dish.getIngredientCount());
            preparedStatement.executeUpdate();

            addIngredientList(connection, dish);
            addDishIdAndIngredientId(connection, dish);

        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return dish;
    }

    private void addDishIdAndIngredientId(Connection connection, Dish dish) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (INSERT_QUERY_DEPENDENCY)) {

            Iterator<Ingredient> iterator = dish.getIngredients().iterator();
            while (iterator.hasNext()) {
                Ingredient ingred = iterator.next();
                preparedStatement.setString(2, ingred.getIngredientId().toString());
                preparedStatement.setString(1, dish.getDishId().toString());
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ingredient> addIngredientList(Connection connection, Dish dish) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (INSERT_QUERY_INGREDIENT)) {
            Iterator<Ingredient> iterator = dish.getIngredients().iterator();
            while (iterator.hasNext()) {
                Ingredient ingred = iterator.next();
                preparedStatement.setString(1, ingred.getIngredientId().toString());
                preparedStatement.setString(2, ingred.getIngredientName());
                preparedStatement.setInt(3, ingred.getIngredientCalories());
                preparedStatement.addBatch();
                preparedStatement.executeBatch();
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<UUID> getIngredientsIdForDish(Connection connection, String dishId) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (SELECT_FROM_INGREDIENTS_ID_FOR_DISH)) {
            preparedStatement.setString(1, dishId);
            ResultSet resultSet = preparedStatement.executeQuery();
            List<UUID> list = new ArrayList<>();
            while (resultSet.next()) {
                String item = resultSet.getString("main_ingredient_id");
                list.add(UUID.fromString(item));
            }
            return list;

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Ingredient> getIngredientsByIdForDish(Connection connection, List<UUID> ingredientsIdForDish) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (SELECT_FROM_INGREDIENTS_BY_ID)) {
            ResultSet resultSet = null;

            Iterator<UUID> iterator = ingredientsIdForDish.stream().iterator();
            while (iterator.hasNext()) {
                UUID ingred = iterator.next();
                preparedStatement.setString(1, ingred.toString());

                resultSet = preparedStatement.executeQuery();
            }
            assert resultSet != null;
            return new ArrayList<>(resultSetIngredient(resultSet));

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Dish> getAllDish(Connection connection) {
        List<Dish> dishes = new ArrayList<>();
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (SELECT_FROM_DISHES)) {
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetToList(resultSet);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return dishes;
    }

    @Override
    public Dish getDishByName(Connection connection, String dishName) {
        try (PreparedStatement preparedStatement = connection.prepareStatement
                (SELECT_QUERRY_BY_DISH_NAME)) {
            preparedStatement.setString(1, dishName);
            ResultSet resultSet = preparedStatement.executeQuery();
            return resultSetToList(resultSet).stream().findFirst().get();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


    private List<Dish> resultSetToList(ResultSet resultSet) throws SQLException {
        List<Dish> dishes = new ArrayList<>();
        while (resultSet.next()) {
            Dish dish = new Dish();
            dish.setDishId(UUID.fromString(resultSet.getString("dish_id").toString()));
            dish.setDishName(resultSet.getString("dishname"));
            dish.setCaloriesCount(resultSet.getInt("caloriescount"));
            dish.setIngredientCount(resultSet.getInt("ingredientcount"));

            dishes.add(dish);
        }
        return dishes;
    }

    private List<Ingredient> resultSetIngredient(ResultSet resultSet) throws SQLException {
        List<Ingredient> ingredients = new ArrayList<>();
        while (resultSet.next()) {
            Ingredient ingredient = new Ingredient();
            ingredient.setIngredientId(UUID.fromString(resultSet.getString("ingredient_id")));
            ingredient.setIngredientName(resultSet.getString("ingredient_name"));
            ingredient.setIngredientCalories(resultSet.getInt("ingredient_calories"));

            ingredients.add(ingredient);
        }
        return ingredients;
    }
}
