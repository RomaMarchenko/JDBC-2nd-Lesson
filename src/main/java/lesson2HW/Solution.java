package lesson2HW;

import java.sql.*;
import java.util.ArrayList;

public class Solution {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.ceffzvpakwhb.us-east-2.rds.amazonaws.com:1521:ORCL";

    private static final String USER = "main";
    private static final String PASS = "main2001";


    public ArrayList<Product> getAllProducts(){
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT")) {
                while (resultSet.next()) {
                    Product product = new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getProductsByPrice(){
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT WHERE PRICE < 100")) {
                while (resultSet.next()) {
                    Product product = new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE"));
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getProductsByDescription(){
        ArrayList<Product> products = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(DB_URL, USER, PASS); Statement statement = connection.createStatement()) {
            try (ResultSet resultSet = statement.executeQuery("SELECT * FROM PRODUCT")) {
                while (resultSet.next()) {
                    Product product = new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE"));
                    if (product.getDescription().length() > 50)
                    products.add(product);
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return products;
    }
}
