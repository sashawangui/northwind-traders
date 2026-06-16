import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public static void main(String[] args) {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:mysql://localhost:3306/northwind");
        dataSource.setUsername("root");
        dataSource.setPassword("password");

        Scanner scanner = new Scanner(System.in);

        System.out.println("What do you want to do?");
        System.out.println("1) Display all products");
        System.out.println("2) Display all customers");
        System.out.println("0) Exit");
        System.out.print("Select an option: ");

        int choice = scanner.nextInt();

        switch (choice) {
            case 1 -> displayProducts(dataSource);
            case 2 -> displayCustomers(dataSource);
            case 0 -> System.out.println("Goodbye!");
            default -> System.out.println("Invalid option.");
        }
        scanner.close();
    }

    public static void displayProducts(BasicDataSource dataSource) {

        String sql = """
                SELECT ProductID,
                       ProductName,
                       UnitPrice,
                       UnitsInStock
                FROM Products
                """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()
        ) {

            System.out.printf("%-5s %-35s %-10s %-10s%n",
                    "ID", "Name", "Price", "Stock");

            while (results.next()) {

                int id = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");

                System.out.printf("%-5d %-35s $%-9.2f %-10d%n",
                        id, name, price, stock);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayCustomers(BasicDataSource dataSource) {

        String sql = """
                SELECT ContactName,
                       CompanyName,
                       City,
                       Country,
                       Phone
                FROM Customers
                ORDER BY Country
                """;

        try (
                Connection connection = dataSource.getConnection();
                PreparedStatement statement = connection.prepareStatement(sql);
                ResultSet results = statement.executeQuery()
        ) {

            System.out.printf("%-25s %-35s %-20s %-15s %-20s%n",
                    "Contact Name",
                    "Company",
                    "City",
                    "Country",
                    "Phone");

            while (results.next()) {

                String contactName = results.getString("ContactName");
                String companyName = results.getString("CompanyName");
                String city = results.getString("City");
                String country = results.getString("Country");
                String phone = results.getString("Phone");

                System.out.printf("%-25s %-35s %-20s %-15s %-20s%n",
                        contactName,
                        companyName,
                        city,
                        country,
                        phone);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }