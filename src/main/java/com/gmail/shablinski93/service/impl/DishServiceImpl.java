package com.gmail.shablinski93.service.impl;

import com.gmail.shablinski93.model.Dish;
import com.gmail.shablinski93.model.Ingredient;
import com.gmail.shablinski93.repository.DishRepository;
import com.gmail.shablinski93.repository.impl.DishRepoImpl;
import com.gmail.shablinski93.service.DataIterationService;
import com.gmail.shablinski93.service.DishService;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DishServiceImpl implements DishService {
    private final DishRepository dishRepository = new DishRepoImpl();
    private final DataIterationService dataIterationService = new DataIterationServiceImpl();
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
        System.out.println("Введите наименование блюда: ");
        String name = dataIterationService.lineReader();
        String approvedDishName = inputIsValid(name, ERROR_MESSAGE_DISH_NAME, REGEX_LETTERS);
        dish.setDishName(approvedDishName);

        System.out.println("Введите калорийность блюда: ");
        String calories = dataIterationService.lineReader();
        String approvedCaloriesInput = inputIsValid(calories, ERROR_MESSAGE_CALORIES, REGEX_NUMBERS);
        dish.setCaloriesCount(Integer.valueOf(approvedCaloriesInput));

        System.out.println("Введите количество ингредиентов блюда: ");
        String ingredientCountString = dataIterationService.lineReader();
        String approvedIngredientsCount = inputIsValid(ingredientCountString, ERROR_MESSAGE_INGREDIENTS_COUNT, REGEX_NUMBERS);
        dish.setIngredientCount(Integer.valueOf(approvedIngredientsCount));
        dish.setDishId(UUID.randomUUID());

        List<Ingredient> ingredients = new ArrayList<Ingredient>();
        for (int i = 0; i < Integer.valueOf(approvedIngredientsCount); i++) {
            ingredients.add(createIngredient());
        }
        dish.setIngredients(ingredients);
        dishRepository.addDish(dish);
        return dish;
    }

    @Override
    public Ingredient createIngredient() {
        Ingredient ingredient = new Ingredient();
        System.out.println("Введите наименование ингредиента: ");
        String name = dataIterationService.lineReader();
        String approvedIngredientName = inputIsValid(name, ERROR_MESSAGE_INGREDIENT_NAME, REGEX_LETTERS);
        ingredient.setIngredientName(approvedIngredientName);

        System.out.println("Введите калорийность ингредиента: ");
        String calories = dataIterationService.lineReader();
        String approvedIngredientCalories = inputIsValid(calories, ERROR_MESSAGE_INGREDIENT_CALORIES, REGEX_NUMBERS);
        ingredient.setIngredientCalories(Integer.valueOf(approvedIngredientCalories));
        ingredient.setIngredientId(UUID.randomUUID());
        return ingredient;
    }

    @Override
    public List<String> getAllDishesNames() {
        List<Dish> dishes = dishRepository.getAllDish();
        return dishes.stream().map(Dish::getDishName).collect(Collectors.toList());
    }

    @Override
    public Dish findDishByName(String dishName) {
        String validDishName = inputIsValid(dishName, ERROR_MESSAGE_DISH_NAME, REGEX_LETTERS);
        Dish dish = dishRepository.getDishByName(validDishName);

        List<UUID> ingredientsIdForDish = dishRepository.getIngredientsIdForDish(dish.getDishId().toString());
        List<Ingredient> ingredientsByIdForDish = dishRepository.getIngredientsByIdForDish(ingredientsIdForDish);
        dish.setIngredients(ingredientsByIdForDish);
        return dish;
    }

    @Override
    public List<Dish> sortDishByCalories() {
        return dishRepository.getAllDish()
                .stream()
                .sorted(Comparator.comparing(Dish::getCaloriesCount))
                .collect(Collectors.toList());
    }

    @Override
    public List<Dish> sortDishByName() {
        return dishRepository.getAllDish()
                .stream()
                .sorted(Comparator.comparing(Dish::getDishName))
                .collect(Collectors.toList());
    }

    @Override
    public void saveInFile() {
        dataIterationService.createFileWithInfoFromDb("all-info.txt");
        List<String> allDish = getAllDishesNames();
        dataIterationService.saveInFile("all-info.txt", allDish);
    }

    private String inputIsValid(String name, String errorMessage, String regex) {
        while (name == null || name.isBlank() || name.length() > 20 || !name.matches(regex)) {
            System.out.println(errorMessage);
            name = dataIterationService.lineReader();
        }
        return name;
    }
}