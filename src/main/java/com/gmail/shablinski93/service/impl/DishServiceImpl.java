package com.gmail.shablinski93.service.impl;

import com.gmail.shablinski93.Dish;
import com.gmail.shablinski93.Ingredient;
import com.gmail.shablinski93.repository.ConnectionRepository;
import com.gmail.shablinski93.repository.DishRepository;
import com.gmail.shablinski93.repository.impl.ConnectionRepositoryImpl;
import com.gmail.shablinski93.repository.impl.DishRepoImpl;
import com.gmail.shablinski93.service.DishService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.lang.invoke.MethodHandles;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class DishServiceImpl implements DishService {
    private static final Logger logger = LogManager.getLogger(MethodHandles.lookup().lookupClass());
    private static DishService instance;
    private final ConnectionRepository connectionRepository = ConnectionRepositoryImpl.getInstance();
    private final DishRepository dishRepository = DishRepoImpl.getInstance();

    public static DishService getInstance() {
        if (instance == null) {
            instance = new DishServiceImpl();
        }
        return instance;
    }


    @Override
    public Dish createDish() throws IOException {
        Dish dish = new Dish();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите наименование блюда: ");
        String name = reader.readLine();
        dish.setDishName(name);

        System.out.println("Введите калорийность блюда: ");//rename
        String сalories = reader.readLine();
        dish.setCaloriesCount(Integer.valueOf(сalories));

        System.out.println("Введите количество ингредиентов блюда: ");//rename
        String ingredientCountString = reader.readLine();
        Integer ingredientCount = Integer.valueOf(ingredientCountString);
        dish.setIngredientCount(ingredientCount);
        dish.setDishId(UUID.randomUUID());

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.add(createIngredient());
        }

        dish.setIngredients(ingredients);

        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            dishRepository.addDish(connection, dish);
            connection.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }

        return dish;
    }


    @Override
    public Ingredient createIngredient() throws IOException {
        Ingredient ingredient = new Ingredient();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите наименование ингредиент: ");
        String name = reader.readLine();
        ingredient.setIngredientName(name);

        System.out.println("Введите калорийность ингредиента: ");
        String calories = reader.readLine();
        ingredient.setIngredientCalories(Integer.valueOf(calories));

        ingredient.setIngredientId(UUID.randomUUID());
        return ingredient;
    }

    @Override
    public List<Dish> getAllDish() {
        List<Dish> dishes;
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            dishes = dishRepository.getAllDish(connection);
            connection.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            return null;
        }
        return dishes;

    }

    @Override
    public Dish findDishByName(String dishName) {
        Dish dish = new Dish();
        try (Connection connection = connectionRepository.getConnection()) {
            connection.setAutoCommit(false);
            dish = dishRepository.getDishByName(connection, dishName);

            List<UUID> ingredientsIdForDish = dishRepository.getIngredientsIdForDish(connection, dish.getDishId().toString());
            List<Ingredient> ingredientsByIdForDish = dishRepository.getIngredientsByIdForDish(connection, ingredientsIdForDish);
            dish.setIngredients(ingredientsByIdForDish);

            connection.commit();
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
        }
        return dish;
    }

    @Override
    public void saveInFile() {
        createFileWithInfoFromDb();
        try {
            FileOutputStream fos = new FileOutputStream("all-info.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            List<Dish> allDish = getAllDish();
            oos.writeObject(allDish);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void createFileWithInfoFromDb() {
        try {
            File file = new File("all-info.txt");
            if (file.createNewFile()) {
                System.out.println("Файл создан");
            } else {
                System.out.println("Файл уже существует");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}
