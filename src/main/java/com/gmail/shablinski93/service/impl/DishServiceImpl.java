package com.gmail.shablinski93.service.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.ConnectionRepository;
import com.gmail.shablinski93.repository.DishRepository;
import com.gmail.shablinski93.repository.impl.ConnectionRepositoryImpl;
import com.gmail.shablinski93.repository.impl.DishRepoImpl;
import com.gmail.shablinski93.service.DishService;
import com.gmail.shablinski93.service.exception.ConnectionException;
import com.gmail.shablinski93.service.exception.DataReaderException;

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
    private static final String REGEX_LETTERS = "^[A-Za-z]+";
    private static final String REGEX_NUMBERS = "^[0-9]*$";
    public static final String ERROR_MESSAGE_DISH_NAME = "Ошибка. Введите наименование блюда снова:";
    public static final String ERROR_MESSAGE_CALORIES = "Ошибка. Введите кол-во калорий снова:";
    public static final String ERROR_MESSAGE_INGREDIENTS_COUNT = "Ошибка. Введите кол-во ингредиентов снова:";
    public static final String ERROR_MESSAGE_INGREDIENT_NAME = "Ошибка. Введите название ингредиента снова:";
    public static final String ERROR_MESSAGE_INGREDIENT_CALORIES = "Ошибка. Введите калорийность ингредиента снова:";

    @Override
    public Dish createDish() {
        Dish dish = new Dish();
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Введите наименование блюда: ");
            String name = reader.readLine();
            String approvedDishName = inputIsValid(name, ERROR_MESSAGE_DISH_NAME, REGEX_LETTERS);
            dish.setDishName(approvedDishName);

            System.out.println("Введите калорийность блюда: ");
            String calories = reader.readLine();
            String approvedCaloriesInput = inputIsValid(calories, ERROR_MESSAGE_CALORIES, REGEX_NUMBERS);
            dish.setCaloriesCount(Integer.valueOf(approvedCaloriesInput));

            System.out.println("Введите количество ингредиентов блюда: ");
            String ingredientCountString = reader.readLine();
            String approvedIngredientsCount = inputIsValid(ingredientCountString, ERROR_MESSAGE_INGREDIENTS_COUNT, REGEX_NUMBERS);
            dish.setIngredientCount(Integer.valueOf(approvedIngredientsCount));
            dish.setDishId(UUID.randomUUID());

            List<Ingredient> ingredients = new ArrayList<Ingredient>();
            for (int i = 0; i < Integer.valueOf(approvedIngredientsCount); i++) {
                ingredients.add(createIngredient());
            }
            dish.setIngredients(ingredients);

            Connection connection = connectionRepository.getConnection();
            connection.setAutoCommit(false);
            dishRepository.addDish(connection, dish);
            connection.commit();
        } catch (IOException e) {
            throw new DataReaderException();
        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return dish;
    }

    @Override
    public Ingredient createIngredient() {
        Ingredient ingredient = new Ingredient();
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Введите наименование ингредиента: ");
            String name = reader.readLine();
            String approvedIngredientName = inputIsValid(name, ERROR_MESSAGE_INGREDIENT_NAME, REGEX_LETTERS);
            ingredient.setIngredientName(approvedIngredientName);

            System.out.println("Введите калорийность ингредиента: ");
            String calories = reader.readLine();
            String approvedIngredientCalories = inputIsValid(calories, ERROR_MESSAGE_INGREDIENT_CALORIES, REGEX_NUMBERS);
            ingredient.setIngredientCalories(Integer.valueOf(approvedIngredientCalories));
            ingredient.setIngredientId(UUID.randomUUID());
        } catch (IOException ex) {
            throw new DataReaderException();
        }
        return ingredient;
    }

    @Override
    public List<String> getAllDishesNames() {
        Connection connection = connectionRepository.getConnection();
        List<Dish> dishes = new ArrayList<>();
        try {
            connection.setAutoCommit(false);
            dishes = dishRepository.getAllDish(connection);
        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return dishes.stream().map(Dish::getDishName).collect(Collectors.toList());
    }

    @Override
    public Dish findDishByName(String dishName) {
        Dish dish = new Dish();
        Connection connection = connectionRepository.getConnection();
        try {
            connection.setAutoCommit(false);
            String validDishName = inputIsValid(dishName, ERROR_MESSAGE_DISH_NAME, REGEX_LETTERS);
            dish = dishRepository.getDishByName(connection, validDishName);

            List<UUID> ingredientsIdForDish = dishRepository.getIngredientsIdForDish(connection, dish.getDishId().toString());
            List<Ingredient> ingredientsByIdForDish = dishRepository.getIngredientsByIdForDish(connection, ingredientsIdForDish);
            dish.setIngredients(ingredientsByIdForDish);
        } catch (SQLException e) {
            throw new ConnectionException();
        } catch (NullPointerException e) {
            System.out.println("Dish not exist. Please, try again");
        }
        return dish;
    }

    @Override
    public List<Dish> sortDishByCalories() {
        List<Dish> allDish = new ArrayList<>();
        Connection connection = connectionRepository.getConnection();
        try {
            connection.setAutoCommit(false);
            allDish = dishRepository.getAllDish(connection)
                    .stream()
                    .sorted(Comparator.comparing(Dish::getCaloriesCount))
                    .collect(Collectors.toList());
            return allDish;
        } catch (SQLException e) {
            throw new ConnectionException();
        }
    }

    @Override
    public List<Dish> sortDishByName() {
        List<Dish> allDish = new ArrayList<>();
        Connection connection = connectionRepository.getConnection();
        try {
            connection.setAutoCommit(false);
            allDish = dishRepository.getAllDish(connection)
                    .stream()
                    .sorted(Comparator.comparing(Dish::getDishName))
                    .collect(Collectors.toList());
        } catch (SQLException e) {
            throw new ConnectionException();
        }
        return allDish;
    }

    @Override
    public void saveInFile() {
        createFileWithInfoFromDb();
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream("all-info.txt");
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            List<String> allDish = getAllDishesNames();
            oos.writeObject(allDish);
            oos.flush();
            oos.close();
        } catch (IOException e) {
            throw new DataReaderException();
        }
    }

    private void createFileWithInfoFromDb() {
        File file = new File("all-info.txt");
        try {
            if (file.createNewFile()) {
                System.out.println("Файл создан");
            } else {
                System.out.println("Файл уже существует");
            }
        } catch (IOException e) {
            throw new DataReaderException();
        }
    }

    private String inputIsValid(String name, String errorMessage, String regex) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        try {
            while (name == null || name.isBlank() || name.length() > 20 || !name.matches(regex)) {
                System.out.println(errorMessage);
                name = reader.readLine();
            }
        } catch (IOException e) {
            throw new DataReaderException();
        }
        return name;
    }
}