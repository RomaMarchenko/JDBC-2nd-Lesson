package lesson2HW;

import java.sql.*;
import java.util.ArrayList;

public class Solution {
    private static final String DB_URL = "jdbc:oracle:thin:@gromcode-lessons.ceffzvpakwhb.us-east-2.rds.amazonaws.com:1521:ORCL";

    private static final String USER = "main";
    private static final String PASS = "main2001";

    private final String getAllProducts = "SELECT * FROM PRODUCT";
    private final String getProductsByPrice = "SELECT * FROM PRODUCT WHERE PRICE < 100";
    private final String getProductsWithDescMoreThan50 = "SELECT * FROM PRODUCT WHERE LENGTH(DESCRIPTION) > 50";
    private final String getProductsWithDescMoreThan100 = "SELECT * FROM PRODUCT WHERE LENGTH(DESCRIPTION) > 100";
    private final String increasePrice = "UPDATE PRODUCT SET PRICE = PRICE + 100 WHERE PRICE < 970";
    private final String updateDescriptionById = "UPDATE PRODUCT SET DESCRIPTION = (?) WHERE ID = (?)";

    private static Connection sqlConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL, USER, PASS);
    }


    public ArrayList<Product> getAllProducts() {
        ArrayList<Product> products = new ArrayList<>();
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(getAllProducts)) {
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

    public ArrayList<Product> getProductsByPrice() {
        ArrayList<Product> products = new ArrayList<>();
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(getProductsByPrice)) {
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

    public ArrayList<Product> getProductsByDescription() {
        ArrayList<Product> products = new ArrayList<>();
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(getProductsWithDescMoreThan50)) {
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

    public void increasePrice() {
        try (Statement statement = sqlConnection().createStatement()) {
            statement.executeUpdate(increasePrice);
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }

    public void changeDescription() {
        try (Statement statement = sqlConnection().createStatement()) {
            try (ResultSet resultSet = statement.executeQuery(getProductsWithDescMoreThan100)) {
                while (resultSet.next()) {
                    Product product = new Product(resultSet.getLong("ID"), resultSet.getString("NAME"), resultSet.getString("DESCRIPTION"), resultSet.getInt("PRICE"));
                    String[] allSentences = product.getDescription().split("\\. ");
                    StringBuilder newDescription = new StringBuilder();
                    for (int index = 0; index < allSentences.length - 1; index++) {
                        newDescription.append(allSentences[index]).append(". ");
                    }
                    newDescription = new StringBuilder(newDescription.toString().trim());
                    long productId = product.getId();
                    try (PreparedStatement preparedStatement = sqlConnection().prepareStatement(updateDescriptionById)) {
                        preparedStatement.setString(1, String.valueOf(newDescription).trim());
                        preparedStatement.setLong(2, productId);
                        preparedStatement.executeUpdate();
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("Something went wrong");
            e.printStackTrace();
        }
    }
}
