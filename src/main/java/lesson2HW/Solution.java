package lesson2HW;

import java.sql.*;
import java.util.ArrayList;

public class Solution {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.ceffzvpakwhb.us-east-2.rds.amazonaws.com:1521:ORCL";

    private static final String USER = "main";
    private static final String PASS = "main2001";

    private final String GET_ALL_PRODUCTS = "SELECT * FROM PRODUCT";
    private final String GET_PRODUCTS_BY_PRICE = "SELECT * FROM PRODUCT WHERE PRICE < 100";
    private final String GET_PRODUCTS_WITH_DESCRIPTION_LONGER_50 = "SELECT * FROM PRODUCT WHERE LENGTH(DESCRIPTION) > 50";
    private final String GET_PRODUCTS_WITH_DESCRIPTION_LONGER_100 = "SELECT * FROM PRODUCT WHERE LENGTH(DESCRIPTION) > 100";
    private final String INCREASE_PRICE = "UPDATE PRODUCT SET PRICE = PRICE + 100 WHERE PRICE < 970";
    private final String UPDATE_DESCRIPTION_BY_ID = "UPDATE PRODUCT SET DESCRIPTION = (?) WHERE ID = (?)";


    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(GET_ALL_PRODUCTS)) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getProductsByPrice() {
        ArrayList<Product> products = new ArrayList<>();
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(GET_PRODUCTS_BY_PRICE)) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return products;
    }

    public ArrayList<Product> getProductsByDescription() {
        ArrayList<Product> products = new ArrayList<>();
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(GET_PRODUCTS_WITH_DESCRIPTION_LONGER_50)) {
                while (resultSet.next()) {
                    products.add(mapProduct(resultSet));
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
        return products;
    }

    public void increasePrice() {
        try (Statement statement = sqlConnection().createStatement()) {
            statement.executeUpdate(INCREASE_PRICE);
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    public void changeDescription() {
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(GET_PRODUCTS_WITH_DESCRIPTION_LONGER_100)) {
                while (resultSet.next()) {
                    try (PreparedStatement preparedStatement = sqlConnection().prepareStatement(UPDATE_DESCRIPTION_BY_ID)) {
                        preparedStatement.setString(1, String.valueOf(editDescription(resultSet)).trim());
                        preparedStatement.setLong(2, mapProduct(resultSet).getId());
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    private static Connection sqlConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }

    private Product mapProduct(ResultSet resultSet) throws SQLException {
        Product product = new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE"));
        return product;
    }

    private StringBuilder editDescription (ResultSet resultSet) throws SQLException {
        String[] allSentences = mapProduct(resultSet).getDescription().split("\\. ");
        StringBuilder newDescription = new StringBuilder();
        for (int index = 0; index < allSentences.length - 1; index++) {
            newDescription.append(allSentences[index]).append(". ");
        }
        return new StringBuilder(newDescription.toString().trim());
    }
}
