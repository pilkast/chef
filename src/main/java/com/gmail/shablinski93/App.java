package com.gmail.shablinski93;

import com.gmail.shablinski93.service.impl.DishServiceImpl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;

public class App {


    public static void main(String[] args) throws IOException, SQLException {


        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Выберите нужное из списка:");
            System.out.println("1.Показать список блюд");
            System.out.println("2.Добавить блюдо");
            System.out.println("3.Показать ");
            System.out.println("4.Посмотреть блюдо по названию");
            System.out.println("5.Выход из прилаги");
            System.out.println("6.Сохранить данные файл");
            String num = reader.readLine();
            switch (num) {
                case "1":
                    List<Dish> dishes = DishServiceImpl.getInstance().getAllDish();
                    System.out.println(dishes);
                    break;
                case "2":
                    DishServiceImpl.getInstance().createDish();
                    break;
                case "4":
                    System.out.println("Введите наименование блюда: ");
                    String name = reader.readLine();
                    System.out.println(DishServiceImpl.getInstance().findDishByName(name));
                    break;
                case "6":
                    DishServiceImpl.getInstance().saveInFile();
            }
        }

//        Scanner scanner = new Scanner(System.in);
//
//        Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
//
//        while (true) {
//            System.out.println("1.Показать список блюдов");
//            System.out.println("2.Добавить блюдо");
//            System.out.println("3.Посмотреть блюдо по айди");
//            System.out.println("4.Выход из прилаги");
//
//            int command = scanner.nextInt();
//
//            if (command == 1) {
//                Statement statement = connection.createStatement();
//                String sqlSelect = "select * from dish order by id desc";
//                ResultSet resultSet = statement.executeQuery(sqlSelect);
//                while (resultSet.next()) {
//                    System.out.println("id: " + resultSet.getInt("id") + " dish name:" +
//                            resultSet.getString("dishname") + " calories: " +
//                            resultSet.getString("caloriescount") +
//                            " ingredients: " + resultSet.getString("ingredientcount"));
//                }
//            } else if (command == 2) {
//                Statement statement = connection.createStatement();
//                String sqlInsert = "insert into dish (id, dishname, caloriescount, ingredientcount) values (?, ?, ?, ?)";
//                PreparedStatement preparedStatement = connection.prepareStatement(sqlInsert);
//
//                System.out.println("enter ID:");
//                int dishId = scanner.nextInt();
//                preparedStatement.setInt(1, dishId);
//
//                System.out.println("enter dish name:");
//                String dishName = scanner.next();
//                preparedStatement.setString(2, dishName);
//
//                System.out.println("enter calories count:");
//                String caloriesCount = scanner.next();
//                preparedStatement.setString(3, caloriesCount);
//
//                System.out.println("enter ingredients:");
//                scanner.nextLine();
//                String ingredients = scanner.nextLine();
//                preparedStatement.setString(4, ingredients);
//
//                preparedStatement.executeUpdate();
//
//            } else if (command == 4) {
//                System.exit(0);
//            }
//        }
    }

//    private static void addDishes() throws IOException {
//        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
//        System.out.println("Введите наименование блюда: ");
//        String name = reader.readLine();
//        DishModel dish = new DishModel();
//        dish.setDishName(name);
//
//        System.out.println("Введите колларицнгсть блюда: ");//rename
//        String сalories = reader.readLine();
//        dish.setCaloriesCount(сalories);
//
//        System.out.println("Введите ингоидиентк блюда: ");//rename
//        String ingredient = reader.readLine();
//        dish.setIngredientCount(ingredient);


    // Factory.getInstance().getDepartmentDAO().addDepartment(dish);
}