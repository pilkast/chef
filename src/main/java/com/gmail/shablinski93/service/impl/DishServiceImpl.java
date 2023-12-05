package com.gmail.shablinski93.service.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.ConnectionRepository;
import com.gmail.shablinski93.repository.DishRepository;
import com.gmail.shablinski93.repository.impl.ConnectionRepositoryImpl;
import com.gmail.shablinski93.repository.impl.DishRepoImpl;
import com.gmail.shablinski93.service.DishService;

import java.io.*;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DishServiceImpl implements DishService {
    private final ConnectionRepository connectionRepository = ConnectionRepositoryImpl.getInstance();
    private final DishRepository dishRepository = new DishRepoImpl();

    @Override
    public Dish createDish() throws IOException, SQLException {
        Dish dish = new Dish();

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("Введите наименование блюда: ");
        String name = reader.readLine();
        dish.setDishName(name);

        System.out.println("Введите калорийность блюда: ");
        String calories = reader.readLine();
        dish.setCaloriesCount(Integer.valueOf(calories));

        System.out.println("Введите количество ингредиентов блюда: ");
        String ingredientCountString = reader.readLine();
        Integer ingredientCount = Integer.valueOf(ingredientCountString);
        dish.setIngredientCount(ingredientCount);
        dish.setDishId(UUID.randomUUID());

        List<Ingredient> ingredients = new ArrayList<Ingredient>();

        for (int i = 0; i < ingredientCount; i++) {
            ingredients.add(createIngredient());
        }

        dish.setIngredients(ingredients);

        Connection connection = connectionRepository.getConnection();
        connection.setAutoCommit(false);
        dishRepository.addDish(connection, dish);
        connection.commit();
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
    public List<String> getAllDishesNames() throws SQLException {
        Connection connection = connectionRepository.getConnection();
        connection.setAutoCommit(false);
        List<Dish> dishes = dishRepository.getAllDish(connection);
        List<String> dishNames = dishes.stream().map(Dish::getDishName).collect(Collectors.toList());
        connection.commit();
        return dishNames;
    }

    @Override
    public Dish findDishByName(String dishName) throws SQLException {
        Dish dish = new Dish();
        Connection connection = connectionRepository.getConnection();
        connection.setAutoCommit(false);
        dish = dishRepository.getDishByName(connection, dishName);

        List<UUID> ingredientsIdForDish = dishRepository.getIngredientsIdForDish(connection, dish.getDishId().toString());
        List<Ingredient> ingredientsByIdForDish = dishRepository.getIngredientsByIdForDish(connection, ingredientsIdForDish);
        dish.setIngredients(ingredientsByIdForDish);
        connection.commit();
        return dish;
    }

    @Override
    public List<Dish> sortDishByCalories() throws SQLException {
        List<Dish> allDish = new ArrayList<>();
        Connection connection = connectionRepository.getConnection();
        connection.setAutoCommit(false);
        allDish = dishRepository.getAllDish(connection)
                .stream()
                .sorted(Comparator.comparing(Dish::getCaloriesCount))
                .collect(Collectors.toList());
        connection.commit();
        return allDish;
    }

    @Override
    public List<Dish> sortDishByName() throws SQLException {
        List<Dish> allDish = new ArrayList<>();
        Connection connection = connectionRepository.getConnection();
        connection.setAutoCommit(false);
        allDish = dishRepository.getAllDish(connection)
                .stream()
                .sorted(Comparator.comparing(Dish::getDishName))
                .collect(Collectors.toList());
        connection.commit();
        return allDish;
    }

    @Override
    public void saveInFile() throws IOException, SQLException {
        createFileWithInfoFromDb();
        FileOutputStream fos = new FileOutputStream("all-info.txt");
        ObjectOutputStream oos = new ObjectOutputStream(fos);
        List<String> allDish = getAllDishesNames();
        oos.writeObject(allDish);
        oos.flush();
        oos.close();
    }

    private void createFileWithInfoFromDb() throws IOException {

        File file = new File("all-info.txt");
        if (file.createNewFile()) {
            System.out.println("Файл создан");
        } else {
            System.out.println("Файл уже существует");
        }
    }
}