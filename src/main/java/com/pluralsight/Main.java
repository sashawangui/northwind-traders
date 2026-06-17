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
    dataSource.setPassword("yearup26");

    Scanner scanner = new Scanner(System.in);
    int choice;

    do {
        System.out.println("\nWhat do you want to do?");
        System.out.println("1) Display all products");
        System.out.println("2) Display all customers");
        System.out.println("3) Display categories and products by category");
        System.out.println("0) Exit");
        System.out.print("Select an option: ");

        while (!scanner.hasNextInt()) {
            System.out.println("Please enter a number.");
            scanner.next();
            System.out.print("Select an option: ");
        }
        choice = scanner.nextInt();

        switch (choice) {
            case 1 -> displayProducts(dataSource);
            case 2 -> displayCustomers(dataSource);
            case 3 -> {
                displayCategories(dataSource);
                System.out.print("Enter a category ID to view its products: ");

                    while (!scanner.hasNextInt()) {
                        System.out.println("Please enter a number.");
                        scanner.next();
                        System.out.print("Enter a category ID: ");
                    }
                int categoryId = scanner.nextInt();
                displayProductsByCategory(dataSource, categoryId);
            }
            case 0 -> System.out.println("Goodbye!");
            default -> System.out.println("Invalid option. Please try again.");
        }
    } while (choice != 0);

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

    public static void displayCategories(BasicDataSource dataSource) {
        String sql = "SELECT CategoryID, CategoryName FROM Categories ORDER BY CategoryID";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            System.out.println("\nAll Categories:");
            System.out.printf("%-12s %-40s%n", "Category ID", "Category Name");
            System.out.println("--------------------------------------------");
            while (results.next()) {
                int id = results.getInt("CategoryID");
                String name = results.getString("CategoryName");
                System.out.printf("%-12d %-40s%n", id, name);
            }
            System.out.println(); // blank line for readability

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void displayProductsByCategory(BasicDataSource dataSource, int categoryId) {

    String sql = """
                SELECT ProductID,
                       ProductName,
                       UnitPrice,
                       UnitsInStock
                FROM Products
                WHERE CategoryID = ?
                ORDER BY ProductName
                """;

    try (Connection connection = dataSource.getConnection();
         PreparedStatement statement = connection.prepareStatement(sql)) {

        statement.setInt(1, categoryId);

        try (ResultSet results = statement.executeQuery()) {

            System.out.printf("\nProducts in category %d:\n", categoryId);
            System.out.printf("%-5s %-35s %-10s %-10s%n",
                    "ID", "Name", "Price", "Stock");

            boolean found = false;
            while (results.next()) {
                found = true;
                int id = results.getInt("ProductID");
                String name = results.getString("ProductName");
                double price = results.getDouble("UnitPrice");
                int stock = results.getInt("UnitsInStock");
                System.out.printf("%-5d %-35s $%-9.2f %-10d%n",
                        id, name, price, stock);
            }
            if (!found) {
                System.out.println("No products found for this category.");
            }
        }

    } catch (SQLException e) {
        e.printStackTrace();
    }
}
