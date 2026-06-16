package com.pluralsight;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");

        dataSource.setUsername("root");
        dataSource.setPassword("yearup26");

        String sql =
                """
                SELECT ProductID,
                       ProductName,
                       UnitPrice,
                       UnitsInStock
                FROM Products
                """;

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int id =
                        resultSet.getInt("ProductID");

                String name =
                        resultSet.getString("ProductName");

                double price =
                        resultSet.getDouble("UnitPrice");

                int stock =
                        resultSet.getInt("UnitsInStock");

                System.out.println("Product ID: " + id);
                System.out.println("Name: " + name);
                System.out.println("Price: " + price);
                System.out.println("Stock: " + stock);
                System.out.println("----------------");
            }
        }
        catch(SQLException e)
        {e.printStackTrace();}
    }
}
