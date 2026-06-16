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
                "SELECT ProductName FROM products";

        try (Connection connection = dataSource.getConnection()) {
            PreparedStatement statement =
                    connection.prepareStatement(sql);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                System.out.println(productName);
            }
        }
        catch(SQLException e)
        {e.printStackTrace();}
    }
}
