package com.gmail.shablinski93.repository.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.ConnectionRepository;
import com.gmail.shablinski93.repository.DbInteractionService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public class DbInteractionServiceImpl implements DbInteractionService {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private final ConnectionRepository connectionRepository = ConnectionRepositoryImpl.getInstance();

    @Override
    public ResultSet executeQueryOneParam(String sqlQuery, Integer paramIndex, String paramData) {
        ResultSet resultSet = null;
        try {
            Connection connection = connectionRepository.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            preparedStatement.setString(paramIndex, paramData);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error("Some problem with SQL request", e.getMessage(), e);
        }
        return resultSet;
    }

    @Override
    public ResultSet executeQueryNoParam(String sqlQuery) {
        ResultSet resultSet = null;
        try {
            Connection connection = connectionRepository.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(sqlQuery);
            resultSet = preparedStatement.executeQuery();
        } catch (SQLException e) {
            logger.error("Some problem with SQL request", e.getMessage(), e);
        }
        return resultSet;
    }

    @Override
    public boolean executeUpdateDish(String insertQueryEntity, HashMap<Integer, String> mapEntity) {
        int countOfLines = 0;
        try {
            Connection connection = connectionRepository.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement
                    (insertQueryEntity);
            preparedStatement.setString(1, mapEntity.get(1));
            preparedStatement.setString(2, mapEntity.get(2));
            preparedStatement.setInt(3, Integer.parseInt(mapEntity.get(3)));
            preparedStatement.setInt(4, Integer.parseInt(mapEntity.get(4)));
            countOfLines = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            logger.error("Some problem with save data in dish table", e.getMessage(), e);
        }
        return countOfLines != 0;
    }

    @Override
    public void executeUpdateIngredient(String insertQueryIngredient, List<Ingredient> entityList) {
        try {
            Connection connection = connectionRepository.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(insertQueryIngredient);
            for (Ingredient ingred : entityList) {
                preparedStatement.setString(1, ingred.getIngredientId().toString());
                preparedStatement.setString(2, ingred.getIngredientName());
                preparedStatement.setInt(3, ingred.getIngredientCalories());
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("Some problem with save data in ingredient table", e.getMessage(), e);
        }
    }

    @Override
    public void executeUpdateDependency(String insertQueryDependency, HashMap<Integer, List<String>> dependencyValue) {
        try {
            Connection connection = connectionRepository.getConnection();
            connection.setAutoCommit(false);

            PreparedStatement preparedStatement = connection.prepareStatement(insertQueryDependency);
            for (int i = 0; i < dependencyValue.get(2).size(); i++) {
                preparedStatement.setString(1, dependencyValue.get(1).get(0));
                preparedStatement.setString(2, dependencyValue.get(2).get(i));
                preparedStatement.executeUpdate();
            }
            connection.commit();
        } catch (SQLException e) {
            logger.error("Some problem with save data in dependency table", e.getMessage(), e);
        }
    }

    @Override
    public List<UUID> getIngredientsId(ResultSet resultSet) {
        List<UUID> collectId = new ArrayList<>();
        while (true) {
            try {
                if (!resultSet.next()) break;
                String item = resultSet.getString("main_ingredient_id");
                collectId.add(UUID.fromString(item));
            } catch (SQLException e) {
                logger.error("Some problem with select IDs from ingredients table", e.getMessage(), e);
            }
        }
        return collectId;
    }

    @Override
    public List<Dish> resultSetToList(ResultSet resultSet) {
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
                logger.error("Some problem with data received from DB.", e.getMessage(), e);
            }
        }
        return dishes;
    }

    @Override
    public Ingredient oneIngredient(ResultSet resultSet) {
        Ingredient ingredient = new Ingredient();
        while (true) {
            try {
                if (!resultSet.next())
                    break;
                ingredient.setIngredientId(UUID.fromString(resultSet.getString("ingredient_id")));
                ingredient.setIngredientName(resultSet.getString("ingredient_name"));
                ingredient.setIngredientCalories(resultSet.getInt("ingredient_calories"));
            } catch (SQLException e) {
                logger.error("Some problem with data received from ingredients table", e.getMessage(), e);
            }
        }
        return ingredient;
    }
}